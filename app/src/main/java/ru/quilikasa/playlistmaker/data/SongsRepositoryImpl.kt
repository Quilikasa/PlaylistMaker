package ru.quilikasa.playlistmaker.data

import ru.quilikasa.playlistmaker.data.dto.SearchSongsRequest
import ru.quilikasa.playlistmaker.data.dto.SearchSongsResponse
import ru.quilikasa.playlistmaker.domain.api.SongsRepository
import ru.quilikasa.playlistmaker.domain.models.SearchSongsResult
import ru.quilikasa.playlistmaker.domain.models.Track

class SongsRepositoryImpl(private val networkClient: NetworkClient) : SongsRepository {

    override fun searchSongs(trackName: String): SearchSongsResult {
        return try {
            val response = networkClient.doRequest(SearchSongsRequest(trackName))
            if (response.resultCode in 200..300) {
                if((response as SearchSongsResponse).results.isEmpty()) {
                    SearchSongsResult.Empty
                } else {
                    val tracks = response.results.map { Track(it.trackName, it.artistName, it.trackTimeMillis,
                            it.artworkUrl100, it.getCoverArtwork(), it.trackId, it.collectionName,
                            it.releaseDate, it.extractYearFromIsoDate(), it.primaryGenreName,
                            it.country, it.previewUrl, 0) }
                    SearchSongsResult.Success(tracks)
                }
            } else {
                SearchSongsResult.Error(response.resultCode)
            }
        } catch (e: Exception) {
            SearchSongsResult.Error(-1)
        }
    }
}