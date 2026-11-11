package ru.quilikasa.playlistmaker.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.quilikasa.playlistmaker.data.HistoryRepositoryImpl
import ru.quilikasa.playlistmaker.data.SettingsRepositoryImpl
import ru.quilikasa.playlistmaker.data.SongsRepositoryImpl
import ru.quilikasa.playlistmaker.domain.api.HistoryRepository
import ru.quilikasa.playlistmaker.domain.api.SettingsRepository
import ru.quilikasa.playlistmaker.domain.api.SongsRepository

val repositoryModule = module {

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(named("StorageClientTrackDbDto")))
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get(named("StorageClientBoolean")))
    }

    single<SongsRepository> {
        SongsRepositoryImpl(get())
    }
}