package ru.quilikasa.playlistmaker

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.quilikasa.playlistmaker.SearchActivity.Companion.KEY_TRACK_EXTRA
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.Year
import java.util.Locale

class AudioplayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var btnPlay: ImageView
    private lateinit var txtTime: TextView

    private val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    private val handler = Handler(Looper.getMainLooper())
    private val runnable: Runnable = Runnable {
        txtTime.text = formatter.format(mediaPlayer.currentPosition)
        handler.postDelayed(runnable, 300)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        track = intent.getSerializableExtra(KEY_TRACK_EXTRA) as Track

        preparePlayer()

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        val imgAlbum = findViewById<ImageView>(R.id.img_album)
        Glide.with(this)
            .load(track?.getCoverArtwork())
            .placeholder(R.drawable.album)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8.0f, this)))
            .into(imgAlbum)

        val textAlbum = findViewById<TextView>(R.id.text_album)
        textAlbum.text = track?.trackName

        val textArtist = findViewById<TextView>(R.id.text_artist)
        textArtist.text = track?.artistName

        btnPlay = findViewById(R.id.img_play)
        btnPlay.setOnClickListener {
            playbackControl()
        }

        txtTime = findViewById(R.id.text_time)

        val textTimeTotal = findViewById<TextView>(R.id.text_time_total)
        textTimeTotal.text = formatter.format(track?.trackTimeMillis)

        val textAlbumBottom = findViewById<TextView>(R.id.text_album_bottom)
        textAlbumBottom.text = track?.collectionName

        val textYear = findViewById<TextView>(R.id.text_year)
        textYear.text = extractYearFromIsoDate(track?.releaseDate)

        val textGenre = findViewById<TextView>(R.id.text_genre)
        textGenre.text = track?.primaryGenreName

        val textCountry = findViewById<TextView>(R.id.text_country)
        textCountry.text = track?.country
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            btnPlay.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            btnPlay.setImageResource(R.drawable.button_play)
            playerState = STATE_PREPARED
            handler.removeCallbacks(runnable)
            txtTime.text = "00:00"
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        btnPlay.setImageResource(R.drawable.button_pause)
        playerState = STATE_PLAYING
        handler.postDelayed(runnable, 300)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        btnPlay.setImageResource(R.drawable.button_play)
        playerState = STATE_PAUSED
        handler.removeCallbacks(runnable)
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

    private fun extractYearFromIsoDate(dateString: String?): String {
        val instant = Instant.parse(dateString)
        return Year.from(instant.atOffset(java.time.ZoneOffset.UTC)).value.toString()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(runnable)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}