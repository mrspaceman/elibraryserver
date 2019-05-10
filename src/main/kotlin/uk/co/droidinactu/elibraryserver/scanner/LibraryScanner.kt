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
import java.util.function.Consumer

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

        FileUtils.getFileList(rootdir).forEach(Consumer<String> { readFileData(File(it), rootdir) })

        logger.info("LibraryScanner::scanLibraryForEbooks($rootdir) finished")
    }

    private fun readFileData(file: File, librootdir: String) {
        logger.debug("LibraryScanner::readFileData(${file.name}) started")
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
