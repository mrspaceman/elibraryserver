package uk.co.droidinactu.elibraryserver.data

import org.apache.solr.client.solrj.beans.Field
import java.util.*
import kotlin.collections.HashSet

/**
 * Created by aspela on 31/08/16.
 */
class EBook() {

    companion object {
        const val ID_FIELD = "id"
        const val FILETYPES_FIELD = "filetypes"
        const val AUTHORS_FIELD = "authors"
        const val TAGS_FIELD = "tags"
        const val METADATA_FIELD = "metadata_*"
        const val FULLFILENAME_FIELD = "fullfiledirname"
        const val FILEDIR_FIELD = "fileDir"
        const val FILENAME_FIELD = "fileName"
        const val BOOKTITLE_FIELD = "booktitle"
        const val BOOKSUMMARY_FIELD = "booksummary"
        const val BOOKISBN_FIELD = "isbn"
        const val BOOKSERIES_FIELD = "bookseries"
    }

    @Field(ID_FIELD)
    var id: String? = null

    @Field(FILETYPES_FIELD)
    var filetypes = HashSet<FileType>()

    @Field(AUTHORS_FIELD)
    var authors = mutableListOf<Author>()

    @Field(TAGS_FIELD)
    var tags = HashSet<String>()

    @Field(METADATA_FIELD)
    val metadata = HashMap<String, String>()

    @Field(FULLFILENAME_FIELD)
    var fullFileDirName = ""

    @Field(FILEDIR_FIELD)
    var fileDir = ""

    @Field(FILENAME_FIELD)
    var fileName = ""

    @Field
    var rating = 0

    @Field
    var lastModified: Long = 0

    @Field
    var publicationDate: Long = 0

    @Field(BOOKTITLE_FIELD)
    var bookTitle = ""

    @Field(BOOKSUMMARY_FIELD)
    var bookSummary = ""

    @Field(BOOKISBN_FIELD)
    var bookIsbn = ""

    @Field(BOOKSERIES_FIELD)
    var bookSeries = ""

    @Field
    var bookSeriesIdx = -1

    var coverImage: ByteArray? = null

    /**
     * Copy constructor.
     */
    constructor(rhs: EBook) : this() {
        fullFileDirName = rhs.fullFileDirName
        fileDir = rhs.fileDir
        fileName = rhs.fileName
        rating = rhs.rating
        coverImage = rhs.coverImage
    }

//    fun setCoverImageFromBitmap(coverImage: Bitmap) {
//        val coverWidthMax = 600
//        val coverHeightMax = 900
//        val stream = ByteArrayOutputStream()
//        if (coverImage.width > coverWidthMax || coverImage.height > coverHeightMax) {
//            val scaledCvrImg = resizeBitmapFitXY(coverWidthMax, coverHeightMax, coverImage)
//            scaledCvrImg.compress(Bitmap.CompressFormat.PNG, 90, stream)
//        } else {
//            coverImage.compress(Bitmap.CompressFormat.PNG, 90, stream)
//        }
//        this.coverImage = stream.toByteArray()
//    }


    fun addTag(pTag: String) {
        if (!tags.contains(pTag)) {
            this.tags.add(pTag)
        }
    }

    fun addFileType(pFiletype: FileType) {
        if (!filetypes.contains(pFiletype)) {
            filetypes.add(pFiletype)
        }
    }

    fun addFileTypes(pFiletype: MutableSet<FileType>) {
        filetypes.addAll(pFiletype)
    }

    fun addFileType(pFiletype: String) {
        filetypes.add(FileType.valueOf(pFiletype))
    }

    fun addAuthor(author: Author) {
        authors.add(author)
    }

    fun addAuthor(author: String) {
        if (author.contains(" ")) {
            val authornames = author.split(" ")
            authors.add(Author(authornames[0], authornames[1]))
        } else {
            authors.add(Author(author))
        }
    }

    fun addMetadataEntry(name: String, value: String) {
        metadata.put(name, value)
    }

}
