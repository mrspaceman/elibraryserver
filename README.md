# Sample Spring Boot Solr REST application

This is a simple [spring boot](https://spring.io) application that

1. walks a directory tree looking for ebook files (pdf, epub, mobi)
1. extracts metadata for each file (using [apache tika](https://tika.apache.org/))
1. sends the metadata to an [apache solr](https://lucene.apache.org/solr/) server
1. publishes a REST API
   * `/ebook`
   * `/ebook/rescan`
   * `/ebook/{ebookid}`
   * `/ebook/search/{searchTerm}`
   * `/ebook/search/{searchTerm}/{page}`


