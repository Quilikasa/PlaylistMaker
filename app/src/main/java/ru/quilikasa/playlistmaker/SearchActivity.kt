package ru.quilikasa.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SearchActivity : AppCompatActivity() {

    private var searchText: String = ""

    private lateinit var editText: EditText
    private lateinit var placeholderLayout: ViewGroup
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var placeholderButton: Button
    private lateinit var searchList: RecyclerView

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

        val tracksAdapter = TrackAdapter()
        searchList.adapter = tracksAdapter

        btnBack.setOnClickListener {
            finish()
        }

        btnClear.setOnClickListener {
            editText.setText("")
            tracksAdapter.setTracks(listOf())
            tracksAdapter.notifyDataSetChanged()
            placeholderLayout.visibility = View.GONE

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
        }

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    btnClear.visibility = View.GONE
                } else {
                    searchText = s.toString()
                    btnClear.visibility = View.VISIBLE
                }
            }
        }

        editText.addTextChangedListener(textWatcher)

        val retrofitCallback = object : Callback<SearchResult>{
            override fun onResponse(
                call: Call<SearchResult?>,
                response: Response<SearchResult?>
            ) {
                if (response.code() == 200) {
                    if (response.body()?.results?.isNotEmpty() == true) {
                        placeholderLayout.visibility = View.GONE
                        searchList.visibility = View.VISIBLE

                        tracksAdapter.setTracks(response.body()?.results!!)
                        tracksAdapter.notifyDataSetChanged()
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

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                itunesApiService.searchSongs(searchText).enqueue(retrofitCallback)
            }
            false
        }

        placeholderButton.setOnClickListener {
            itunesApiService.searchSongs(searchText).enqueue(retrofitCallback)
        }
    }

    private fun showPlaceholder(isFailure: Boolean) {
        placeholderLayout.visibility = View.VISIBLE
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(KEY).toString()
        editText.setText(searchText)
    }

    companion object {
        private const val KEY = "EditText"
    }
}