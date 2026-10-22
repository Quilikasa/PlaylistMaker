package ru.quilikasa.playlistmaker

import ru.quilikasa.playlistmaker.data.SongsRepositoryImpl
import ru.quilikasa.playlistmaker.data.network.RetrofitNetworkClient
import ru.quilikasa.playlistmaker.domain.api.SongsInteractor
import ru.quilikasa.playlistmaker.domain.api.SongsRepository
import ru.quilikasa.playlistmaker.domain.impl.SongsInteractorImpl

object Creator {

    private fun getSongsRepository(): SongsRepository {
        return SongsRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSongsInteractor(): SongsInteractor {
        return SongsInteractorImpl(getSongsRepository())
    }
}