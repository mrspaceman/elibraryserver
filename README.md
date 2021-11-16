# Sample Spring Boot REST application

This is a simple [spring boot](https://spring.io) application that

1. walks a directory tree looking for ebook files (pdf, epub, mobi)
1. extracts metadata for each file (using [apache tika](https://tika.apache.org/))
1. sends the metadata to an [apache solr](https://lucene.apache.org/solr/) server
1. publishes a REST API
   * GET `/ebooks` - a list of book titles and authors
   * GET `/ebooks/folders` - get a list of the library folders
   * POST `/ebooks/folders` - add a folder to the list of the library folders
   * DELETE `/ebooks/folders/{folderid}` - remove a folder from the list of the library folders
   * POST `/ebooks/folders/rescan` - triggers a rescan of the library folders
   * GET `/ebooks/{ebookid}` - gets the full information for the specified book
   * GET `/ebooks/search/{searchTerm}` -
   * GET `/ebooks/search/{searchTerm}/{page}` - 


