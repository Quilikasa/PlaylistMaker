package ru.quilikasa.playlistmaker.domain.models

import java.io.Serializable

data class Track(val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val artworkUrl500: String,
    val trackId: Long,
    val collectionName: String,
    val releaseDate: String,
    val releaseYear: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    var viewDate: Long,
    ) : Serializable {
}
