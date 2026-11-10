package ru.quilikasa.playlistmaker.presentation.settings

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.quilikasa.playlistmaker.Creator
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY

class SettingsViewModel(private val context: Context) : ViewModel() {

    private val settingsInteractor = Creator.provideSettingsInteractor(context)
    private val stateLiveData = MutableLiveData(settingsInteractor.isDarkTheme())
    fun observeState(): LiveData<Boolean> = stateLiveData

    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as Application)
                SettingsViewModel(app)
            }
        }
    }

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