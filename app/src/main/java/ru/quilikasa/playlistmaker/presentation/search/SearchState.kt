package ru.quilikasa.playlistmaker.presentation.search

import ru.quilikasa.playlistmaker.domain.models.Track

sealed class SearchState {
    data class Success(val tracks: List<Track>) : SearchState()
    data class Error(val errorCode: Int) : SearchState()
    object EmptySearch : SearchState()
    object Loading : SearchState()
    object EmptyScreen : SearchState()
}