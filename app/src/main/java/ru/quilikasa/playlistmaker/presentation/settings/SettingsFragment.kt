package ru.quilikasa.playlistmaker.presentation.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.quilikasa.playlistmaker.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingsViewModel by viewModel<SettingsViewModel>()

        val themeSwitcher = view.findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            settingsViewModel.switchTheme(checked)
        }

        val btnShare = view.findViewById<FrameLayout>(R.id.btn_share)
        btnShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
            startActivity(intent)
        }

        val btnSupport = view.findViewById<FrameLayout>(R.id.btn_support)
        btnSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = "mailto:".toUri()
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject));
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_text));
            startActivity(intent);
        }

        val btnEula = view.findViewById<FrameLayout>(R.id.btn_eula)
        btnEula.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, getString(R.string.eula_link).toUri())
            startActivity(intent)
        }

        settingsViewModel.observeState().observe(getViewLifecycleOwner()) {
            themeSwitcher.isChecked = it
        }
    }
}