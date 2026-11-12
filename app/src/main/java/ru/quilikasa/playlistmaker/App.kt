package ru.quilikasa.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.quilikasa.playlistmaker.di.dataModule
import ru.quilikasa.playlistmaker.di.interactorModule
import ru.quilikasa.playlistmaker.di.repositoryModule
import ru.quilikasa.playlistmaker.di.viewModelModule
import ru.quilikasa.playlistmaker.domain.api.SettingsInteractor

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        val settingsInteractor: SettingsInteractor by inject()
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