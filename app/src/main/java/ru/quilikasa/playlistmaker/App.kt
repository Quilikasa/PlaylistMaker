package ru.quilikasa.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(PLAYLIST_PREFERENCES, MODE_PRIVATE)
        val darkTheme = sharedPrefs.getBoolean(THEME_PREFERENCE_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        val sharedPrefs = getSharedPreferences(PLAYLIST_PREFERENCES, MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(THEME_PREFERENCE_KEY, darkThemeEnabled).apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val PLAYLIST_PREFERENCES = "playlistmaker_preferences"
        const val THEME_PREFERENCE_KEY = "theme_preference_key"
        const val HISTORY_PREFERENCE_KEY = "history_preference_key"
    }
}