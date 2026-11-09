package ru.quilikasa.playlistmaker.presentation.player

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.quilikasa.playlistmaker.R
import ru.quilikasa.playlistmaker.presentation.search.SearchActivity.Companion.KEY_TRACK_EXTRA
import ru.quilikasa.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity : AppCompatActivity() {
    private lateinit var audioPlayerViewModel: AudioplayerViewModel

    private val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val track = intent.getSerializableExtra(KEY_TRACK_EXTRA) as Track

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        val imgAlbum = findViewById<ImageView>(R.id.img_album)
        Glide.with(this)
            .load(track.artworkUrl500)
            .placeholder(R.drawable.album)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8.0f, this)))
            .into(imgAlbum)

        val textAlbum = findViewById<TextView>(R.id.text_album)
        textAlbum.text = track.trackName

        val textArtist = findViewById<TextView>(R.id.text_artist)
        textArtist.text = track.artistName

        val btnPlay = findViewById<ImageView>(R.id.img_play)
        btnPlay.setOnClickListener {
            audioPlayerViewModel.playbackControl()
        }

        val txtTime = findViewById<TextView>(R.id.text_time)

        val textTimeTotal = findViewById<TextView>(R.id.text_time_total)
        textTimeTotal.text = formatter.format(track.trackTimeMillis)

        val textAlbumBottom = findViewById<TextView>(R.id.text_album_bottom)
        textAlbumBottom.text = track.collectionName

        val textYear = findViewById<TextView>(R.id.text_year)
        textYear.text = track.releaseYear

        val textGenre = findViewById<TextView>(R.id.text_genre)
        textGenre.text = track.primaryGenreName

        val textCountry = findViewById<TextView>(R.id.text_country)
        textCountry.text = track.country

        audioPlayerViewModel = ViewModelProvider(
            this,
            AudioplayerViewModel.getFactory(track.previewUrl)
        ).get(AudioplayerViewModel::class.java)

        audioPlayerViewModel.observeProgressTime().observe(this) {
            txtTime.text = it
        }

        audioPlayerViewModel.observePlayerState().observe(this) {
            if(it == AudioplayerViewModel.STATE_PLAYING) {
                btnPlay.setImageResource(R.drawable.button_pause)
            } else {
                btnPlay.setImageResource(R.drawable.button_play)
            }
            btnPlay.isEnabled = it != AudioplayerViewModel.STATE_DEFAULT
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