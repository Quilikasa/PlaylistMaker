package ru.quilikasa.playlistmaker

import android.content.Context
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val track = intent.getSerializableExtra(KEY_TRACK_EXTRA) as? Track

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
        textAlbum.text = track?.collectionName

        val textArtist = findViewById<TextView>(R.id.text_artist)
        textArtist.text = track?.artistName

        val textTimeTotal = findViewById<TextView>(R.id.text_time_total)
        val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
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
}