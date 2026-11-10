package ru.quilikasa.playlistmaker.presentation.player

import ru.quilikasa.playlistmaker.presentation.player.AudioplayerViewModel.Companion.STATE_DEFAULT

data class PlayerState(val playerState: Int = STATE_DEFAULT,
                       val progressTime: String = "00:00")