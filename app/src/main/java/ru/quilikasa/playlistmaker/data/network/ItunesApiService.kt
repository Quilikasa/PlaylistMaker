package ru.quilikasa.playlistmaker.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.quilikasa.playlistmaker.data.dto.SearchSongsResponse

interface ItunesApiService {

    @GET("/search?entity=song")
    fun searchSongs(@Query("term") text: String) : Call<SearchSongsResponse>

    companion object {
        const val BASE_URL = "https://itunes.apple.com"
    }
}