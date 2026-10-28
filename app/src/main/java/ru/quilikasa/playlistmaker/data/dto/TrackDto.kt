package ru.quilikasa.playlistmaker.data.dto

import java.time.Instant
import java.time.Year
import java.time.ZoneOffset

data class TrackDto(val trackName: String,
               val artistName: String,
               val trackTimeMillis: Long,
               val artworkUrl100: String,
               val trackId: Long,
               val collectionName: String,
               val releaseDate: String,
               val primaryGenreName: String,
               val country: String,
               val previewUrl: String,
    ) {

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    fun extractYearFromIsoDate(): String {
        val instant = Instant.parse(releaseDate)
        return Year.from(instant.atOffset(ZoneOffset.UTC)).value.toString()
    }
}