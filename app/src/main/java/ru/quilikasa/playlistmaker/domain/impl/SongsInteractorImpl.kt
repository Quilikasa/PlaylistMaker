package ru.quilikasa.playlistmaker.domain.impl

import ru.quilikasa.playlistmaker.domain.api.SongsInteractor
import ru.quilikasa.playlistmaker.domain.api.SongsRepository
import java.util.concurrent.Executors

class SongsInteractorImpl(private val repository: SongsRepository) : SongsInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchSongs(
        trackName: String,
        consumer: SongsInteractor.SongsConsumer
    ) {
        executor.execute {
            consumer.consume(repository.searchSongs(trackName))
        }
    }
}