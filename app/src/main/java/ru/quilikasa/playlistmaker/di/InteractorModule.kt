package ru.quilikasa.playlistmaker.di

import org.koin.dsl.module
import ru.quilikasa.playlistmaker.domain.api.HistoryInteractor
import ru.quilikasa.playlistmaker.domain.api.SettingsInteractor
import ru.quilikasa.playlistmaker.domain.api.SongsInteractor
import ru.quilikasa.playlistmaker.domain.impl.HistoryInteractorImpl
import ru.quilikasa.playlistmaker.domain.impl.SettingsInteractorImpl
import ru.quilikasa.playlistmaker.domain.impl.SongsInteractorImpl

val interactorModule = module {

    single<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SongsInteractor> {
        SongsInteractorImpl(get())
    }
}