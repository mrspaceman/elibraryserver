package uk.co.droidinactu.elibraryserver

import org.apache.solr.client.solrj.SolrClient
import org.apache.solr.client.solrj.impl.HttpSolrClient
import org.apache.solr.client.solrj.impl.XMLResponseParser
import org.apache.tika.Tika
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.solr.repository.config.EnableSolrRepositories

@Configuration
@EnableSolrRepositories
class SolrConfig  {

    internal var urlString = "http://localhost:8983/solr"

    @Bean
    fun solrClient(): SolrClient {
        val solr = HttpSolrClient.Builder(urlString).build()
        solr.parser = XMLResponseParser()
        return solr
    }

}
