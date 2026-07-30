package ru.quilikasa.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.quilikasa.playlistmaker.App.Companion.HISTORY_PREFERENCE_KEY

class SearchHistoryStorage(private val prefs: SharedPreferences, private val gson: Gson) {
    private val type = object : TypeToken<List<Track>>() {}.type

    fun addTrack(newTrack: Track) {
        var historyJson = prefs.getString(HISTORY_PREFERENCE_KEY, "")
        var tracks: MutableList<Track>
        if (historyJson?.length == 0) {
            tracks = mutableListOf()
        } else {
            tracks = gson.fromJson(historyJson, type)
        }

        if(tracks.contains(newTrack)) {
            tracks.remove(newTrack)
        }
        tracks.add(newTrack)
        if(tracks.size > MAX_HISTORY_SIZE) {
            tracks.removeAt(0)
        }

        historyJson = gson.toJson(tracks, type)
        prefs.edit().putString(HISTORY_PREFERENCE_KEY, historyJson).apply()
    }

    fun getTracks(): List<Track> {
        val historyJson = prefs.getString(HISTORY_PREFERENCE_KEY, "")
        if (historyJson?.length == 0) return listOf()
        val tracks: MutableList<Track> = gson.fromJson(historyJson, type)
        tracks.reverse()
        return tracks
    }

    fun clearTracks() {
        prefs.edit().putString(HISTORY_PREFERENCE_KEY, "").apply()
    }

    companion object {
        private const val MAX_HISTORY_SIZE = 10
    }
}
