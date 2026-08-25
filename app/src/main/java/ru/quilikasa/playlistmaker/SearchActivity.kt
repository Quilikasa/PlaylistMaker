package ru.quilikasa.playlistmaker

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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.quilikasa.playlistmaker.App.Companion.PLAYLIST_PREFERENCES

class SearchActivity : AppCompatActivity() {

    private var searchText: String = ""

    private lateinit var editText: EditText
    private lateinit var placeholderLayout: ViewGroup
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var placeholderButton: Button
    private lateinit var searchList: RecyclerView
    private lateinit var historyLayout: ViewGroup

    private lateinit var tracksAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private lateinit var historyStorage: SearchHistoryStorage

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequest() }

    private var isClickAllowed = true

    private val retrofit = Retrofit.Builder()
        .baseUrl(ItunesApiService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesApiService = retrofit.create<ItunesApiService>()

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
        searchList = findViewById(R.id.recyclerView)
        historyLayout = findViewById(R.id.history)
        val searchHistoryList = findViewById<RecyclerView>(R.id.historyRecyclerView)
        val historyClearButton = findViewById<Button>(R.id.history_clear_btn)

        historyStorage = SearchHistoryStorage(
            getSharedPreferences(PLAYLIST_PREFERENCES, MODE_PRIVATE), Gson())

        tracksAdapter = TrackAdapter(
            onItemClick = { track ->
                if (clickDebounce()) {
                    historyStorage.addTrack(track)
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
        showHistoryList()

        historyClearButton.setOnClickListener {
            historyStorage.clearTracks()
            showEmptyScreen()
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnClear.setOnClickListener {
            editText.setText("")
            showHistoryList()

            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
        }

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.hasFocus() && s?.isEmpty() == true) {
                    showHistoryList()
                    btnClear.visibility = View.GONE
                } else {
                    //TODO показать прогресс вместо пустого экрана
                    showEmptyScreen()
                    searchText = s.toString()
                    btnClear.visibility = View.VISIBLE
                    searchDebounce()
                }
            }
        }

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
                searchRequest()
            }
        }
    }

    private fun openPlayer(track: Track) {
        val intent = Intent(this, AudioplayerActivity::class.java)
        intent.putExtra(KEY_TRACK_EXTRA, track)
        startActivity(intent)
    }

    val retrofitCallback = object : Callback<SearchResult>{
        override fun onResponse(
            call: Call<SearchResult?>,
            response: Response<SearchResult?>
        ) {
            if (response.code() == 200) {
                if (response.body()?.results?.isNotEmpty() == true) {
                    showSearchList(response.body()?.results!!)
                }
                if (response.body()?.results!!.isEmpty()) {
                    showPlaceholder(false)
                }
            } else {
                showPlaceholder(true)
            }
        }

        override fun onFailure(call: Call<SearchResult?>, t: Throwable) {
            showPlaceholder(true)
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchRequest() {
        itunesApiService.searchSongs(searchText).enqueue(retrofitCallback)
    }

    private fun showSearchList(tracks: List<Track>) {
        tracksAdapter.setTracks(tracks)
        tracksAdapter.notifyDataSetChanged()

        placeholderLayout.visibility = View.GONE
        historyLayout.visibility = View.GONE
        searchList.visibility = View.VISIBLE
    }

    private fun showHistoryList() {
        val historyTracks = historyStorage.getTracks()
        if (historyTracks.size > 0) {
            historyAdapter.setTracks(historyTracks)
            historyAdapter.notifyDataSetChanged()

            placeholderLayout.visibility = View.GONE
            historyLayout.visibility = View.VISIBLE
            searchList.visibility = View.GONE
        } else {
            showEmptyScreen()
        }
    }

    private fun showPlaceholder(isFailure: Boolean) {
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
        placeholderLayout.visibility = View.GONE
        historyLayout.visibility = View.GONE
        searchList.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_EDIT_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(KEY_EDIT_TEXT).toString()
        editText.setText(searchText)
    }

    companion object {
        private const val KEY_EDIT_TEXT = "EditText"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        const val KEY_TRACK_EXTRA = "TrackExtra"
    }
}