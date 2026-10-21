package ru.quilikasa.playlistmaker.data.dto

import ru.quilikasa.playlistmaker.domain.models.Track

data class SearchSongsResponse(val resultCount: Int,
                               val results: List<Track>,
    )
