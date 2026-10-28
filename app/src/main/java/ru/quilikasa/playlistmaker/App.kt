package ru.quilikasa.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ru.quilikasa.playlistmaker.domain.api.SettingsInteractor

class App : Application() {

    private lateinit var interactor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()

        interactor = Creator.provideSettingsInteractor(this)
        switchTheme(interactor.isDarkTheme())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        interactor.switchTheme(darkThemeEnabled)

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