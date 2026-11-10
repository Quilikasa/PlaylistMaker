package ru.quilikasa.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ru.quilikasa.playlistmaker.domain.api.SettingsInteractor

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val settingsInteractor = Creator.provideSettingsInteractor(this)
        setTheme(settingsInteractor.isDarkTheme())
    }

    private fun setTheme(darkThemeEnabled: Boolean) {
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