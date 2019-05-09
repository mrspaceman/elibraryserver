/*
 * Copyright 2019 (C) Andy Aspell-Clark
 *
 * Created on : 08/05/2019 15:01
 * Author     : aaspellc
 *
 */
package uk.co.droidinactu.elibraryserver

import uk.co.droidinactu.elibraryserver.data.EBook

/**
 *
 */
interface EBookService {

    fun scanForFiles(rootDir: String)

    fun addEBook(ebk: EBook)

    fun deleteEBook(id: String)
}
