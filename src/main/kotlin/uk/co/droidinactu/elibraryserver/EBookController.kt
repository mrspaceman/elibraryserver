/*
 * Copyright 2019 (C) Andy Aspell-Clark
 *
 * Created on : 08/05/2019 15:37
 * Author     : aaspellc
 *
 */
package uk.co.droidinactu.elibraryserver

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uk.co.droidinactu.elibraryserver.data.EBook
import uk.co.droidinactu.elibraryserver.data.EBookRepository
import uk.co.droidinactu.elibraryserver.scanner.LibraryScanner
import java.nio.file.Paths
import java.util.*

/*
 * Copyright 2019 (C) Andy Aspell-Clark
 *
 * Created on : 08/05/2019 15:37
 * Author     : aaspellc
 *
 */
@RestController
class EBookController {

    companion object {
        const val baseCollectionName = "ebook"
    }

    @Autowired
    lateinit var ebookRepo: EBookRepository

    @Autowired
    lateinit var libScanner: LibraryScanner

    @GetMapping("/${baseCollectionName}/rescan")
    fun rescan(): ResponseEntity<Any> {
        val ebookPath = Paths.get("z:", "Documents", "ebooks")
        libScanner.scanLibraryForEbooks(ebookPath.toString())
        return ResponseEntity<Any>("", HttpStatus.OK)
    }

    @PostMapping("/${baseCollectionName}")
    fun createEBook(@RequestBody ebook: EBook): String {
        val description = "ebook Created"
        ebookRepo.save(ebook)
        return description
    }

    @GetMapping("/${baseCollectionName}")
    fun readEBook(): List<EBook> {
        var allBks = ebookRepo.findAll()
        var ebkList = ArrayList<EBook>()
        allBks.toCollection(ebkList)
        return ebkList
    }

    @GetMapping("/${baseCollectionName}/{ebookid}")
    fun readEBook(@PathVariable ebookid: String): EBook {
        return ebookRepo.findById(ebookid)
                .orElseThrow({ EbookNotFoundException(ebookid) })
    }

    @PutMapping("/${baseCollectionName}")
    fun updateEBook(@RequestBody ebook: EBook): String {
        val description = "EBook Updated"
        ebookRepo.save(ebook)
        return description
    }

    @DeleteMapping("/${baseCollectionName}/{ebookid}")
    fun deleteEBook(@PathVariable ebookid: String): String {
        val description = "ebook Deleted"
        val findById = ebookRepo.findById(ebookid)
        ebookRepo.delete(findById.get())
        return description
    }

    @GetMapping("/${baseCollectionName}/search/{searchTerm}")
    fun findEBookBySearchTerm(@PathVariable searchTerm: String): List<EBook> {
        return ebookRepo.findByTag(searchTerm)
    }

    @GetMapping("/${baseCollectionName}/search/{searchTerm}/{page}")
    fun findEBookBySearchTerm(
            @PathVariable searchTerm: String,
            @PathVariable page: Int): List<EBook> {
        return ebookRepo.findByTag(searchTerm, PageRequest.of(page, 3)).getContent()
    }

}
