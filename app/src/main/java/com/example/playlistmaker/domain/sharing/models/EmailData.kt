package com.example.playlistmaker.domain.sharing.models

data class EmailData(
    val email: Array<String>,
    val subject: String,
    val text: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmailData

        if (!email.contentEquals(other.email)) return false
        if (subject != other.subject) return false
        if (text != other.text) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.contentHashCode()
        result = 31 * result + subject.hashCode()
        result = 31 * result + text.hashCode()
        return result
    }
}
