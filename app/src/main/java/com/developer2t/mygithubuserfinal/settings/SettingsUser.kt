package com.developer2t.mygithubuserfinal.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.developer2t.mygithubuserfinal.R
import com.developer2t.mygithubuserfinal.notification.NotificationReceiver

class SettingsUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val notificationReceiver = NotificationReceiver()

            val btnLanguage =
                findPreference<Preference>(resources.getString(R.string.change_language))
            btnLanguage?.setOnPreferenceClickListener {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
                true
            }

            val btnDaily = findPreference<Preference>(
                resources.getString(R.string.daily_remainder)
            )
            btnDaily?.setOnPreferenceChangeListener { _, newValue ->
                if (newValue == true) {
                    notificationReceiver.setRemainder(
                        requireContext(),
                        9,
                        5,
                        NotificationReceiver.ID_REMAINDER
                    )
                } else {
                    notificationReceiver.cancelRemainder(
                        requireContext(),
                        NotificationReceiver.ID_REMAINDER
                    )
                }
                true
            }


        }
    }
}