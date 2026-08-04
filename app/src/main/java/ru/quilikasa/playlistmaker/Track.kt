package ru.quilikasa.playlistmaker

import java.io.Serializable

data class Track(val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId: Long,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
    ) : Serializable {

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}
