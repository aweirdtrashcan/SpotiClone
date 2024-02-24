package com.stimply.spoticlone.domain.model

data class Song(
    val imageByteArray: ByteArray,
    val mediaId: String = "",
    val songUrl: String = "",
    val subtitle: String = "",
    val title: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Song

        if (!imageByteArray.contentEquals(other.imageByteArray)) return false
        if (mediaId != other.mediaId) return false
        if (songUrl != other.songUrl) return false
        if (subtitle != other.subtitle) return false
        return title == other.title
    }

    override fun hashCode(): Int {
        var result = imageByteArray.contentHashCode()
        result = 31 * result + mediaId.hashCode()
        result = 31 * result + songUrl.hashCode()
        result = 31 * result + subtitle.hashCode()
        result = 31 * result + title.hashCode()
        return result
    }
}