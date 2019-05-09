package uk.co.droidinactu.elibraryserver

class EbookNotFoundException : RuntimeException {

    constructor(ebookid: String) : super("Could not find ebook " + ebookid) {
    }
}
