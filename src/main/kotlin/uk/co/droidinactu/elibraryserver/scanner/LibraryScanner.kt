package uk.co.droidinactu.elibraryserver.scanner

import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.parser.ParseContext
import org.apache.tika.sax.BodyContentHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import uk.co.droidinactu.elibraryserver.data.EBook
import uk.co.droidinactu.elibraryserver.data.EBookRepository
import java.io.File
import java.io.FileInputStream
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors


/**
 * Created by aspela on 31/08/16.
 */
@Component
class LibraryScanner {

    @Autowired
    lateinit var ebookRepo: EBookRepository

    val parser = AutoDetectParser()
    val handler = BodyContentHandler(-1)
    val context = ParseContext()
    var metadata = Metadata()

    private val logger = LoggerFactory.getLogger(LibraryScanner::class.java)

    fun scanLibraryForEbooks(rootdir: String) {
        //   System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")
        logger.info("LibraryScanner::scanLibraryForEbooks($rootdir) started")

        lateinit var files: List<String>
        run {
            var dirs = findDirs(rootdir)

            files = dirs
                    .stream()
                    .parallel()
                    .flatMap { dir -> findFiles(dir).stream() }
                    .collect(Collectors.toList())
        }
        files
                .stream()
                .forEach { file -> readFileData(File(file), rootdir) }

        logger.info("LibraryScanner::scanLibraryForEbooks($rootdir) finished")
    }

    private fun findDirs(dirname: String): Set<String> {
        logger.debug("ReactiveLibraryScanner::findDirs($dirname) started")
        var dirnames = HashSet<String>()
        var f = File(dirname.trim { it <= ' ' })
        val listOfFiles = f.list()

        dirnames.add(dirname)
        for (dname in listOfFiles) {
            f = File(dirname + File.separator + dname)
            if (f.isDirectory) {
                dirnames.add(dirname + File.separator + dname)
                dirnames.addAll(findDirs(dirname + File.separator + dname))
            }
        }
        return dirnames
    }

    private fun findFiles(dir: String): List<String> {
        logger.debug("ReactiveLibraryScanner::findFiles($dir) started")
        var files = ArrayList<String>()
        var f = File(dir)
        val filelist = arrayListOf<String>()
        filelist.addAll(f.list())

        filelist
                .stream()
                .forEach { filename ->
                    f = File(dir + File.separator + filename)
                    if (f.isFile && (filename.toString().toLowerCase().endsWith("epub")
                                    || filename.toString().toLowerCase().endsWith("pdf")
                                    || filename.toString().toLowerCase().endsWith("mobi")
                                    )
                    ) {
                        files.add(dir + File.separator + filename)
                    }
                }
        return files
    }

    private fun readFileData(file: File, librootdir: String) {
        logger.debug("LibraryScanner::readFileData() started")
        try {
            readFileMetaData(file, librootdir)
        } catch (e: Exception) {
            logger.error("Exception reading book file [" + file.name + "] " + e.message)
        }
    }

    private fun readFileMetaData(file: File, librootdir: String) {
        logger.debug("LibraryScanner::readFile(${file.absolutePath}) started")

        val ebk = EBook()
        ebk.fileDir = file.parent
        ebk.fileName = file.absolutePath.substring(ebk.fileDir.length + 1)
        //  ebk.fileName = FilenameUtils.removeExtension(file.absolutePath.substring(ebk.fileDir.length + 1))
        ebk.bookTitle = ebk.fileName
        ebk.lastModified = file.lastModified()
//        ebk.setCoverImageFromBitmap(
//                BitmapFactory.decodeResource(
//                        ctx.resources,
//                        R.drawable.generic_book_cover
//                )
//        )

        try {
            val parentDir = file.parent
            val prntDir = parentDir.substring(librootdir.length + 1)
            val prntDirPath: Path = Paths.get(prntDir)
            for (p in prntDirPath) {
                ebk.addTag(p.toString())
            }
        } catch (oob: StringIndexOutOfBoundsException) {
            ebk.addTag("none")
        } catch (e: Exception) {
            logger.error("Exception [$e] parsing tags for book [${file.name}]")
        }
//        logger.debug("parsing file [filename: " + file.name + ", size: " + file.length() + "]")

        addMetadataToEbook(ebk, file)

        try {
            ebookRepo.save(ebk)
        } catch (e: Exception) {
            logger.error("Exception [$e] sending ebook to solr")
        }
    }

    fun addMetadataToEbook(ebk: EBook, file: File) {
        try {
            val inputstream = FileInputStream(file)
            parser.parse(inputstream, handler, metadata, context)

            //add ebook metadata
            for (name in metadata.names()) {
                ebk.addMetadataEntry(name.toLowerCase(), metadata.get(name))
                if (name.toLowerCase().contains("title") && metadata.get(name).trim().length > 1) {
                    ebk.bookTitle = metadata.get(name)
                } else if (name.toLowerCase().contains("author")) {
                    ebk.addAuthor(metadata.get(name))
                }
            }
        } catch (e: Throwable) {
            logger.error("Apache Tika exception [${ebk.bookTitle}] : ${e.localizedMessage}", e)
        }
    }

}
