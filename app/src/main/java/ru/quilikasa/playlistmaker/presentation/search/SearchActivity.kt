package ru.quilikasa.playlistmaker.presentation.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ru.quilikasa.playlistmaker.Creator
import ru.quilikasa.playlistmaker.presentation.player.AudioplayerActivity
import ru.quilikasa.playlistmaker.R
import ru.quilikasa.playlistmaker.domain.api.HistoryInteractor
import ru.quilikasa.playlistmaker.domain.models.Track

class SearchActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var placeholderLayout: ViewGroup
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var placeholderButton: Button
    private lateinit var progress: ProgressBar
    private lateinit var searchList: RecyclerView
    private lateinit var historyLayout: ViewGroup

    private lateinit var tracksAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private val songsViewModel: SearchViewModel by viewModels()
    private lateinit var historyInteractor: HistoryInteractor

    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnClear = findViewById<ImageView>(R.id.btn_clear)
        editText = findViewById(R.id.edit)
        placeholderLayout = findViewById(R.id.placeholder)
        placeholderImage = findViewById(R.id.placeholder_img)
        placeholderText = findViewById(R.id.placeholder_txt)
        placeholderButton = findViewById(R.id.placeholder_btn)
        progress = findViewById(R.id.progress)
        searchList = findViewById(R.id.recyclerView)
        historyLayout = findViewById(R.id.history)
        val searchHistoryList = findViewById<RecyclerView>(R.id.historyRecyclerView)
        val historyClearButton = findViewById<Button>(R.id.history_clear_btn)

        songsViewModel.observeState().observe(this) {
            render(it)
        }

        historyInteractor = Creator.provideHistoryInteractor(this)

        tracksAdapter = TrackAdapter(
            onItemClick = { track ->
                if (clickDebounce()) {
                    historyInteractor.addTrack(track)
                    openPlayer(track)
                }
            } )
        searchList.adapter = tracksAdapter

        historyAdapter = TrackAdapter(
            onItemClick = { track ->
                if (clickDebounce()) {
                    openPlayer(track)
                }
            }
        )
        searchHistoryList.adapter = historyAdapter
        //TODO перейти на вью модель?
        showHistoryList()

        //TODO перейти на вью модель?
        historyClearButton.setOnClickListener {
            historyInteractor.clearTracks()
            showEmptyScreen()
        }

        btnBack.setOnClickListener {
            finish()
        }

        //TODO перейти на вью модель?
        btnClear.setOnClickListener {
            editText.setText("")
            showHistoryList()

            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
        }

        //TODO перейти на вью модель?
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.hasFocus() && s?.isEmpty() == true) {
                    showHistoryList()
                    btnClear.visibility = View.GONE
                } else {
                    showEmptyScreen()
                    btnClear.visibility = View.VISIBLE
                    songsViewModel.searchDebounce(s.toString())
                }
            }
        }

        //TODO перейти на вью модель?
        editText.addTextChangedListener(textWatcher)
        editText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && editText.text.isEmpty()) {
                showHistoryList()
            } else {
                showEmptyScreen()
            }
        }

        placeholderButton.setOnClickListener {
            if (clickDebounce()) {
                songsViewModel.searchDebounce("")
            }
        }
    }

    private fun openPlayer(track: Track) {
        val intent = Intent(this, AudioplayerActivity::class.java)
        intent.putExtra(KEY_TRACK_EXTRA, track)
        startActivity(intent)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showProgress()
            is SearchState.Success -> showSearchList(state.tracks)
            is SearchState.Error -> showPlaceholder(true)
            is SearchState.EmptySearch -> showPlaceholder(false)
            SearchState.EmptyScreen -> showEmptyScreen()
        }
    }

    private fun showProgress() {
        progress.visibility = View.VISIBLE
        placeholderLayout.visibility = View.GONE
        historyLayout.visibility = View.GONE
        searchList.visibility = View.GONE
    }

    private fun showSearchList(tracks: List<Track>) {
        tracksAdapter.setTracks(tracks)
        tracksAdapter.notifyDataSetChanged()

        progress.visibility = View.GONE
        placeholderLayout.visibility = View.GONE
        historyLayout.visibility = View.GONE
        searchList.visibility = View.VISIBLE
    }

    //TODO перейти на вью модель
    private fun showHistoryList() {
        val historyTracks = historyInteractor.getTracks()
        if (historyTracks.size > 0) {
            historyAdapter.setTracks(historyTracks)
            historyAdapter.notifyDataSetChanged()

            progress.visibility = View.GONE
            placeholderLayout.visibility = View.GONE
            historyLayout.visibility = View.VISIBLE
            searchList.visibility = View.GONE
        } else {
            showEmptyScreen()
        }
    }

    private fun showPlaceholder(isFailure: Boolean) {
        progress.visibility = View.GONE
        placeholderLayout.visibility = View.VISIBLE
        historyLayout.visibility = View.GONE
        searchList.visibility = View.GONE
        if(isFailure) {
            placeholderImage.setImageResource(R.drawable.ic_search_failure)
            placeholderText.text = getString(R.string.search_failure)
            placeholderButton.visibility = View.VISIBLE
        } else {
            placeholderImage.setImageResource(R.drawable.ic_search_empty)
            placeholderText.text = getString(R.string.search_empty_result)
            placeholderButton.visibility = View.GONE
        }
    }

    private fun showEmptyScreen() {
        progress.visibility = View.GONE
        placeholderLayout.visibility = View.GONE
        historyLayout.visibility = View.GONE
        searchList.visibility = View.GONE
    }

    companion object {
        //TODO где должна обрабатываться задерка отправки запроса?
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        const val KEY_TRACK_EXTRA = "TrackExtra"
    }
}