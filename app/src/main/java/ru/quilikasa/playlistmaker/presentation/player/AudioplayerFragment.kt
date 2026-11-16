package ru.quilikasa.playlistmaker.presentation.player

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import ru.quilikasa.playlistmaker.R
import ru.quilikasa.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerFragment : Fragment() {
    private lateinit var track: Track

    private lateinit var audioPlayerViewModel: AudioplayerViewModel

    private val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())

    companion object {
        private const val ARG_TRACK = "track"

        fun createArgs(track: Track): Bundle =
            bundleOf(ARG_TRACK to track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            track = it.getSerializable(ARG_TRACK) as Track
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_audioplayer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        audioPlayerViewModel = getViewModel {
            parametersOf(track.previewUrl)
        }

        val btnBack = view.findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val imgAlbum = view.findViewById<ImageView>(R.id.img_album)
        Glide.with(this)
            .load(track.artworkUrl500)
            .placeholder(R.drawable.album)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8.0f, requireContext())))
            .into(imgAlbum)

        val textAlbum = view.findViewById<TextView>(R.id.text_album)
        textAlbum.text = track.trackName

        val textArtist = view.findViewById<TextView>(R.id.text_artist)
        textArtist.text = track.artistName

        val btnPlay = view.findViewById<ImageView>(R.id.img_play)
        btnPlay.setOnClickListener {
            audioPlayerViewModel.playbackControl()
        }

        val txtTime = view.findViewById<TextView>(R.id.text_time)

        val textTimeTotal = view.findViewById<TextView>(R.id.text_time_total)
        textTimeTotal.text = formatter.format(track.trackTimeMillis)

        val textAlbumBottom = view.findViewById<TextView>(R.id.text_album_bottom)
        textAlbumBottom.text = track.collectionName

        val textYear = view.findViewById<TextView>(R.id.text_year)
        textYear.text = track.releaseYear

        val textGenre = view.findViewById<TextView>(R.id.text_genre)
        textGenre.text = track.primaryGenreName

        val textCountry = view.findViewById<TextView>(R.id.text_country)
        textCountry.text = track.country

        audioPlayerViewModel.observeUiState().observe(getViewLifecycleOwner()) { state ->
            btnPlay.setImageResource(
                when (state.playerState) {
                    AudioplayerViewModel.STATE_PLAYING -> R.drawable.button_pause
                    else -> R.drawable.button_play
                }
            )
            txtTime.text = state.progressTime
        }
    }

    override fun onPause() {
        super.onPause()
        audioPlayerViewModel.pausePlayer()
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}