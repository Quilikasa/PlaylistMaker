package ru.quilikasa.playlistmaker.domain.api

import ru.quilikasa.playlistmaker.domain.models.Track

interface SongsInteractor {

    fun searchSongs(trackName: String, consumer: SongsConsumer)

    interface SongsConsumer {
        fun consume(foundSongs: List<Track>)
    }
}