package ru.quilikasa.playlistmaker.domain.models

sealed interface SearchSongsResult {
    data class Success(val tracks: List<Track>) : SearchSongsResult
    data class Error(val errorCode: Int) : SearchSongsResult
    object Empty : SearchSongsResult
}