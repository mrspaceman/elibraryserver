package uk.co.droidinactu.elibraryserver

import org.apache.solr.client.solrj.SolrClient
import org.apache.solr.client.solrj.impl.HttpSolrClient
import org.apache.solr.client.solrj.impl.XMLResponseParser
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.data.solr.repository.config.EnableSolrRepositories

@Configuration
@EnableSolrRepositories
@PropertySource("classpath:/application.properties")
class SolrConfig  {

    @Value("\${solr.host}")
    lateinit var urlString: String

    @Bean
    fun solrClient(): SolrClient {
        val solr = HttpSolrClient.Builder(urlString).build()
        solr.parser = XMLResponseParser()
        return solr
    }

}
