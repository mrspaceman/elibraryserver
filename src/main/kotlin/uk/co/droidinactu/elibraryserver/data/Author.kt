package uk.co.droidinactu.elibraryserver.data

class Author() {
    constructor(firstname: String) : this() {
        this.firstname = firstname
    }

    constructor(firstname: String, lastname: String) : this() {
        this.firstname = firstname
        this.lastname = lastname
    }

    var firstname: String? = null

    var lastname: String? = null

    var website: String? = null

    var facebookId: String? = null

    var twitterId: String? = null

    val fullName: String
        get() {
            var authorString = ""
            authorString += " $firstname $lastname "
            //     authorString.trim { it <= ' ' }
            return authorString
        }

    override fun toString(): String {
        return fullName
    }
}
