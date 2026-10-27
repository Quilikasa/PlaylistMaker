package ru.quilikasa.playlistmaker.domain.api

interface SettingsRepository {

    fun isDarkTheme(): Boolean
    fun switchTheme(darkThemeEnabled: Boolean)
}