package ru.quilikasa.playlistmaker.presentation.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.quilikasa.playlistmaker.domain.api.SettingsInteractor

class SettingsViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {
    private val stateLiveData = MutableLiveData(settingsInteractor.isDarkTheme())
    fun observeState(): LiveData<Boolean> = stateLiveData

    fun switchTheme(darkThemeEnabled: Boolean) {
        settingsInteractor.switchTheme(darkThemeEnabled)

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        stateLiveData.postValue(settingsInteractor.isDarkTheme())
    }
}