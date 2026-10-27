package ru.quilikasa.playlistmaker.domain.api

import ru.quilikasa.playlistmaker.domain.models.SearchSongsResult

interface SongsRepository {
    fun searchSongs(trackName: String): SearchSongsResult
}