package ru.quilikasa.playlistmaker.data

import ru.quilikasa.playlistmaker.data.dto.SearchSongsRequest
import ru.quilikasa.playlistmaker.data.dto.SearchSongsResponse
import ru.quilikasa.playlistmaker.domain.api.SongsRepository
import ru.quilikasa.playlistmaker.domain.models.Track

class SongsRepositoryImpl(private val networkClient: NetworkClient) : SongsRepository {

    override fun searchSongs(trackName: String): List<Track> {
        val response = networkClient.doRequest(SearchSongsRequest(trackName))
        if (response.resultCode == 200) {
            return (response as SearchSongsResponse).results.map {
                Track(it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100,
                    it.getCoverArtwork(), it.trackId, it.collectionName,
                    it.releaseDate, it.extractYearFromIsoDate(), it.primaryGenreName,
                    it.country, it.previewUrl) }
        } else {
            return emptyList()
        }
    }
}