package ru.quilikasa.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApiService {

    @GET("/search?entity=song")
    fun searchSongs(@Query("term") text: String) : Call<SearchResult>

    companion object {
        const val BASE_URL = "https://itunes.apple.com"
    }
}