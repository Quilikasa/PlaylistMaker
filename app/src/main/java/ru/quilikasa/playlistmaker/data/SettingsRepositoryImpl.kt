package ru.quilikasa.playlistmaker.data

import ru.quilikasa.playlistmaker.App.Companion.THEME_PREFERENCE_KEY
import ru.quilikasa.playlistmaker.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val storageClient: StorageClient<Boolean>) : SettingsRepository {

    override fun isDarkTheme(): Boolean {
        return storageClient.getItem(THEME_PREFERENCE_KEY, false)
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        storageClient.addItem(darkThemeEnabled, THEME_PREFERENCE_KEY)
    }
}