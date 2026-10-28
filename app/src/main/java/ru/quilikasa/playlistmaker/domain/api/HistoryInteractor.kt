package ru.quilikasa.playlistmaker.domain.api

import ru.quilikasa.playlistmaker.domain.models.Track

interface HistoryInteractor {

    fun addTrack(newTrack: Track)
    fun getTracks(): List<Track>
    fun clearTracks()
}