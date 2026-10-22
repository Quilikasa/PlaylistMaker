package ru.quilikasa.playlistmaker.data.dto

data class SearchSongsResponse(val resultCount: Int,
                               val results: List<TrackDto>,
    ) : Response()
