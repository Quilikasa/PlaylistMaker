package ru.quilikasa.playlistmaker.data

import ru.quilikasa.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}