package ru.quilikasa.playlistmaker.presentation.search

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.quilikasa.playlistmaker.Creator
import ru.quilikasa.playlistmaker.domain.api.HistoryInteractor
import ru.quilikasa.playlistmaker.domain.api.SongsInteractor
import ru.quilikasa.playlistmaker.domain.models.SearchSongsResult
import ru.quilikasa.playlistmaker.domain.models.Track

class SearchViewModel(private val context: Context) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as Application)
                SearchViewModel(app)
            }
        }
    }

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private val songsInteractor: SongsInteractor = Creator.provideSongsInteractor()
    private val historyInteractor: HistoryInteractor = Creator.provideHistoryInteractor(context)

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