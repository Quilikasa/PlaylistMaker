package ru.quilikasa.playlistmaker.data.network

import ru.quilikasa.playlistmaker.data.NetworkClient
import ru.quilikasa.playlistmaker.data.dto.Response
import ru.quilikasa.playlistmaker.data.dto.SearchSongsRequest

class RetrofitNetworkClient(private val apiService: ItunesApiService) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (dto is SearchSongsRequest) {
            val resp = apiService.searchSongs(dto.trackName).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}