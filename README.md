# Sample Spring Boot Solr REST application

This is a simple application that 

1. walks a directory tree looking for ebook files (pdf, epub, mobi) 
1. extracts metadata for each file (using [apache tika](https://tika.apache.org/))
1. sends the metadata to a solr server

it also publishes a rest API:

 * `/ebook`
 * `/ebook/rescan`
 * `/ebook/{ebookid}`
 * `/ebook/search/{searchTerm}`
 * `/ebook/search/{searchTerm}/{page}`


