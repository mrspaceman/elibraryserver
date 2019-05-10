package uk.co.droidinactu.elibraryserver.scanner

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.nio.file.Paths

/*
 * Copyright 2019 (C) Andy Aspell-Clark
 *
 * Created on : 08/05/2019 10:28
 * Author     : aaspellc
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension::class)
class LibraryScannerTest {

    @Autowired
    lateinit var libScnr: LibraryScanner

    @Test
    @Tag("FileScanningTests")
    @DisplayName("Test that file scanning adds documents into solr")
    fun scanLibraryForEbooks() {
        val ebookPath = Paths.get("z:", "Documents", "ebooks")
        libScnr.scanLibraryForEbooks(ebookPath.toString())

    }
}
