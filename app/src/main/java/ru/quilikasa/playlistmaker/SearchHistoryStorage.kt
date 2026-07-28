package ru.quilikasa.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.quilikasa.playlistmaker.App.Companion.HISTORY_PREFERENCE_KEY

class SearchHistoryStorage(private val prefs: SharedPreferences) {

    private val gson = Gson()
    private val type = object : TypeToken<List<Track>>() {}.type

    fun addTrack(newTrack: Track) {
        //TODO проверка на повтор и кол-во
        //где сделать сортировку?

        var historyJson = prefs.getString(HISTORY_PREFERENCE_KEY, "")
        val tracks: MutableList<Track> = gson.fromJson(historyJson, type)
        tracks.add(newTrack)
        historyJson = gson.toJson(tracks, type)
        prefs.edit().putString(HISTORY_PREFERENCE_KEY, historyJson).apply()
    }

    fun getTracks(): List<Track> {
        val historyJson = prefs.getString(HISTORY_PREFERENCE_KEY, "")
        return gson.fromJson(historyJson, type)
    }

    fun clearTracks() {
        prefs.edit().remove(HISTORY_PREFERENCE_KEY).apply()
    }
}
