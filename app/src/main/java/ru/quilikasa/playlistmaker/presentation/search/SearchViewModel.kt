package ru.quilikasa.playlistmaker.presentation.search

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.quilikasa.playlistmaker.Creator
import ru.quilikasa.playlistmaker.domain.api.SongsInteractor
import ru.quilikasa.playlistmaker.domain.models.SearchSongsResult

class SearchViewModel : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private val songsInteractor: SongsInteractor = Creator.provideSongsInteractor()

    private var searchText: String = ""

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable {
        updateState(SearchState.Loading)
        searchRequest()
    }

    val songsConsumer = object : SongsInteractor.SongsConsumer {
        override fun consume(result: SearchSongsResult) {
            when(result) {
                is SearchSongsResult.Success -> handler.post { updateState(SearchState.Success(result.tracks)) }
                is SearchSongsResult.Error -> handler.post { updateState(SearchState.Error(result.errorCode)) }
                is SearchSongsResult.Empty -> handler.post { updateState((SearchState.EmptySearch)) }
            }
        }
    }

    fun searchDebounce(newSearchText: String) {
        if(newSearchText.isNotEmpty()) {
            searchText = newSearchText
        }
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest() {
        Log.d("SearchActivity", "Start searchRequest with text = $searchText")
        songsInteractor.searchSongs(searchText, songsConsumer)
    }

    private fun updateState(state: SearchState) {
        stateLiveData.postValue(state)
    }
}