package ru.quilikasa.playlistmaker

import android.content.Context
import com.google.gson.reflect.TypeToken
import ru.quilikasa.playlistmaker.App.Companion.PLAYLIST_PREFERENCES
import ru.quilikasa.playlistmaker.data.HistoryRepositoryImpl
import ru.quilikasa.playlistmaker.data.SettingsRepositoryImpl
import ru.quilikasa.playlistmaker.data.SongsRepositoryImpl
import ru.quilikasa.playlistmaker.data.dto.TrackDbDto
import ru.quilikasa.playlistmaker.data.network.RetrofitNetworkClient
import ru.quilikasa.playlistmaker.data.storage.PrefStorageClient
import ru.quilikasa.playlistmaker.domain.api.HistoryInteractor
import ru.quilikasa.playlistmaker.domain.api.HistoryRepository
import ru.quilikasa.playlistmaker.domain.api.SettingsInteractor
import ru.quilikasa.playlistmaker.domain.api.SettingsRepository
import ru.quilikasa.playlistmaker.domain.api.SongsInteractor
import ru.quilikasa.playlistmaker.domain.api.SongsRepository
import ru.quilikasa.playlistmaker.domain.impl.HistoryInteractorImpl
import ru.quilikasa.playlistmaker.domain.impl.SettingsInteractorImpl
import ru.quilikasa.playlistmaker.domain.impl.SongsInteractorImpl

object Creator {

    private fun getSongsRepository(): SongsRepository {
        return SongsRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSongsInteractor(): SongsInteractor {
        return SongsInteractorImpl(getSongsRepository())
    }

    private fun getHistoryRepository(context: Context): HistoryRepository {
        val type = object : TypeToken<List<TrackDbDto>>() {}.type
        return HistoryRepositoryImpl(PrefStorageClient(context, PLAYLIST_PREFERENCES, type))
    }

    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        val type = object : TypeToken<Boolean>() {}.type
        return SettingsRepositoryImpl(PrefStorageClient(context, PLAYLIST_PREFERENCES, type))
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }
}