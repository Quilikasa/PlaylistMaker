package ru.quilikasa.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.material.switchmaterial.SwitchMaterial
import ru.quilikasa.playlistmaker.App.Companion.PLAYLIST_PREFERENCES
import ru.quilikasa.playlistmaker.App.Companion.THEME_PREFERENCE_KEY

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val sharedPrefs = getSharedPreferences(PLAYLIST_PREFERENCES, MODE_PRIVATE)
        val darkTheme = sharedPrefs.getBoolean(THEME_PREFERENCE_KEY, false)
        themeSwitcher.isChecked = darkTheme

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        val btnShare = findViewById<FrameLayout>(R.id.btn_share)
        btnShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
            startActivity(intent)
        }

        val btnSupport = findViewById<FrameLayout>(R.id.btn_support)
        btnSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = "mailto:".toUri()
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject));
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_text));
            startActivity(intent);
        }

        val btnEula = findViewById<FrameLayout>(R.id.btn_eula)
        btnEula.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, getString(R.string.eula_link).toUri())
            startActivity(intent)
        }
    }
}