package ru.quilikasa.playlistmaker.domain.api

interface SettingsInteractor {

    fun isDarkTheme(): Boolean
    fun switchTheme(darkThemeEnabled: Boolean)
}