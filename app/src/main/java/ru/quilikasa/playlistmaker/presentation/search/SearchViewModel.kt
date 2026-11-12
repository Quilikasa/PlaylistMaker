package ru.quilikasa.playlistmaker.presentation.search

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.quilikasa.playlistmaker.domain.api.HistoryInteractor
import ru.quilikasa.playlistmaker.domain.api.SongsInteractor
import ru.quilikasa.playlistmaker.domain.models.SearchSongsResult
import ru.quilikasa.playlistmaker.domain.models.Track

class SearchViewModel(private val songsInteractor: SongsInteractor,
                      private val historyInteractor: HistoryInteractor) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private var searchText: String = ""

    private val handler = Handler(Looper.getMainLooper())

    init {
        val history = historyInteractor.getTracks()
        if (history.isEmpty()) {
            updateState(SearchState.EmptyScreen)
        } else {
            updateState(SearchState.History(history))
        }
    }

    private val searchRunnable = Runnable {
        searchRequest()
    }

    private val songsConsumer = object : SongsInteractor.SongsConsumer {
        override fun consume(result: SearchSongsResult) {
            when(result) {
                is SearchSongsResult.Success -> updateState(SearchState.Success(result.tracks))
                is SearchSongsResult.Error -> updateState(SearchState.Error(result.errorCode))
                is SearchSongsResult.Empty -> updateState((SearchState.EmptySearch))
            }
        }
    }

    fun searchDebounce(newSearchText: String) {
        if(newSearchText.isEmpty() || searchText == newSearchText) return
        updateState(SearchState.Loading)
        searchText = newSearchText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun retrySearch() {
        if(searchText.isEmpty()) return
        updateState(SearchState.Loading)
        handler.removeCallbacks(searchRunnable)
        searchRequest()
    }

    private fun searchRequest() {
        Log.d("SearchViewModel", "Start searchRequest with text = $searchText")
        songsInteractor.searchSongs(searchText, songsConsumer)
    }

    private fun updateState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    fun clearHistory() {
        historyInteractor.clearTracks()
        updateState((SearchState.EmptyScreen))
    }

    fun addTrackToHistory(newTrack: Track) {
        historyInteractor.addTrack(newTrack)
    }

    fun clearSearch() {
        handler.removeCallbacks(searchRunnable)
        searchText = ""
        val history = historyInteractor.getTracks()
        if (history.isEmpty()) {
            updateState(SearchState.EmptyScreen)
        } else {
            updateState(SearchState.History(history))
        }
    }

    override fun onCleared() {
        handler.removeCallbacks(searchRunnable)
        super.onCleared()
    }
}