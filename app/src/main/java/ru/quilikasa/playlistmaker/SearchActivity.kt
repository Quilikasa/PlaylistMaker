package ru.quilikasa.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
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
    private var editText: EditText? = null

    private val tracksAdapter = TrackAdapter()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ItunesApiService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesApiService = retrofit.create<ItunesApiService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        val btnClear = findViewById<ImageView>(R.id.btn_clear)
        editText = findViewById(R.id.edit)

        btnClear.setOnClickListener {
            editText?.setText("")

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editText?.windowToken, 0)
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

                    itunesApiService.searchSongs(searchText).enqueue(object  : Callback<SearchResult>{
                        override fun onResponse(
                            call: Call<SearchResult?>,
                            response: Response<SearchResult?>
                        ) {
                            if (response.code() == 200) {
                                tracksAdapter.setTracks(response.body()?.results!!)
                                tracksAdapter.notifyDataSetChanged()
                            } else {

                            }
                        }

                        override fun onFailure(
                            call: Call<SearchResult?>,
                            t: Throwable
                        ) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }

        }
        editText?.addTextChangedListener(textWatcher)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = tracksAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(KEY).toString()
        editText?.setText(searchText)
    }

    companion object {
        private const val KEY = "EditText"
    }
}