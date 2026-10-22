package ru.quilikasa.playlistmaker.domain.api

import ru.quilikasa.playlistmaker.domain.models.Track

interface SongsRepository {
    fun searchSongs(trackName: String) : List<Track>
}