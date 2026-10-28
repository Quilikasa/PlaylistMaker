package ru.quilikasa.playlistmaker.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.quilikasa.playlistmaker.data.NetworkClient
import ru.quilikasa.playlistmaker.data.dto.Response
import ru.quilikasa.playlistmaker.data.dto.SearchSongsRequest
import ru.quilikasa.playlistmaker.BuildConfig

class RetrofitNetworkClient : NetworkClient {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ItunesApiService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesApiService = retrofit.create<ItunesApiService>()

    override fun doRequest(dto: Any): Response {
        if (dto is SearchSongsRequest) {
            val resp = itunesApiService.searchSongs(dto.trackName).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}