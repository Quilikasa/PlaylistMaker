package ru.quilikasa.playlistmaker.domain.impl

import ru.quilikasa.playlistmaker.domain.api.SettingsInteractor
import ru.quilikasa.playlistmaker.domain.api.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {

    override fun isDarkTheme(): Boolean {
        return repository.isDarkTheme()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        repository.switchTheme(darkThemeEnabled)
    }
}