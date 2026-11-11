package ru.quilikasa.playlistmaker.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.quilikasa.playlistmaker.presentation.player.AudioplayerViewModel
import ru.quilikasa.playlistmaker.presentation.search.SearchViewModel
import ru.quilikasa.playlistmaker.presentation.settings.SettingsViewModel

val viewModelModule = module {

    viewModel { (trackUrl: String) ->
        AudioplayerViewModel(trackUrl)
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get())
    }
}