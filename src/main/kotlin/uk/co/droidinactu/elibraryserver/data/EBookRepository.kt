/*
 * Copyright 2019 (C) Andy Aspell-Clark
 *
 * Created on : 07/05/2019 16:51
 * Author     : aaspellc
 *
 */
package uk.co.droidinactu.elibraryserver.data

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.solr.repository.Query
import org.springframework.data.solr.repository.SolrCrudRepository
import org.springframework.stereotype.Component

@Component
interface EBookRepository : SolrCrudRepository<EBook, String> {

    fun findByBookTitle(booktitle: String): List<EBook>

    @Query("tags:*?0*")
    fun findByTag(tag: String, pageable: Pageable): Page<EBook>

    @Query("tags:*?0*")
    fun findByTag(tag: String): List<EBook>

    @Query("tags:*?0* OR booktitle:*?0*")
    fun findGeneralSearch(tag: String, pageable: Pageable): Page<EBook>

    @Query("tags:*?0* OR booktitle:*?0*")
    fun findGeneralSearch(tag: String): List<EBook>

}
