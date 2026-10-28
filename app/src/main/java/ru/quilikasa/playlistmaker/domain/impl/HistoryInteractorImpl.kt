package ru.quilikasa.playlistmaker.domain.impl

import ru.quilikasa.playlistmaker.domain.api.HistoryInteractor
import ru.quilikasa.playlistmaker.domain.api.HistoryRepository
import ru.quilikasa.playlistmaker.domain.models.Track

class HistoryInteractorImpl(private val repository: HistoryRepository) : HistoryInteractor {

    override fun addTrack(newTrack: Track) {
        repository.addTrack(newTrack.copy(viewDate = System.currentTimeMillis()))
    }

    override fun getTracks(): List<Track> {
        return repository.getTracks()
    }

    override fun clearTracks() {
        repository.clearTracks()
    }
}