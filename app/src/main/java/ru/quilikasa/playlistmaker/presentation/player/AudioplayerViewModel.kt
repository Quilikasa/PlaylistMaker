package ru.quilikasa.playlistmaker.presentation.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerViewModel(private val trackUrl: String,
                           private val mediaPlayer: MediaPlayer) : ViewModel() {

    private val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())

    private val uiStateLiveData = MutableLiveData(PlayerState())
    fun observeUiState(): LiveData<PlayerState> = uiStateLiveData

    private val handler = Handler(Looper.getMainLooper())
    private val runnable: Runnable = Runnable {
        updateProgress(formatter.format(mediaPlayer.currentPosition))
        handler.postDelayed(runnable, 300)
    }

    init {
        preparePlayer()
    }

    private fun updateState(transform: (PlayerState) -> PlayerState) {
        val current = uiStateLiveData.value ?: PlayerState()
        uiStateLiveData.postValue(transform(current))
    }

    private fun updatePlayerState(state: Int) =
        updateState { it.copy(playerState = state) }

    private fun updateProgress(time: String) =
        updateState { it.copy(progressTime = time) }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            updatePlayerState(STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            updatePlayerState(STATE_PREPARED)
            updateProgress("00:00")
            handler.removeCallbacks(runnable)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        updatePlayerState(STATE_PLAYING)
        handler.postDelayed(runnable, 300)
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        updatePlayerState(STATE_PAUSED)
        handler.removeCallbacks(runnable)
    }

    fun playbackControl() {
        when(uiStateLiveData.value?.playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        handler.removeCallbacks(runnable)
    }

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}