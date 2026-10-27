package ru.quilikasa.playlistmaker.data

import ru.quilikasa.playlistmaker.App.Companion.HISTORY_PREFERENCE_KEY
import ru.quilikasa.playlistmaker.data.dto.TrackDbDto
import ru.quilikasa.playlistmaker.domain.api.HistoryRepository
import ru.quilikasa.playlistmaker.domain.models.Track

class HistoryRepositoryImpl(private val storageClient: StorageClient<TrackDbDto>) : HistoryRepository {

    override fun addTrack(newTrack: Track) {
        val track = TrackDbDto(newTrack.trackName, newTrack.artistName, newTrack.trackTimeMillis,
            newTrack.artworkUrl100, newTrack.artworkUrl500, newTrack.trackId, newTrack.collectionName,
            newTrack.releaseDate, newTrack.releaseYear, newTrack.primaryGenreName, newTrack.country, newTrack.previewUrl)
        val list = storageClient.getItemList(HISTORY_PREFERENCE_KEY).toMutableList()

        if(list.contains(track)) {
            list.remove(track)
        }
        list.add(track)
        if(list.size > MAX_HISTORY_SIZE) {
            list.removeAt(0)
        }

        storageClient.addItemList(list, HISTORY_PREFERENCE_KEY)
    }

    override fun getTracks(): List<Track> {
        return storageClient.getItemList(HISTORY_PREFERENCE_KEY).map { Track(it.trackName, it.artistName,
            it.trackTimeMillis, it.artworkUrl100, it.artworkUrl500, it.trackId, it.collectionName,
            it.releaseDate, it.releaseYear, it.primaryGenreName, it.country, it.previewUrl) }
    }

    override fun clearTracks() {
        storageClient.clear(HISTORY_PREFERENCE_KEY)
    }

    companion object {
        private const val MAX_HISTORY_SIZE = 10
    }
}