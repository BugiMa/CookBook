package com.example.cookbook.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.*
import com.example.cookbook.R

class SettingsFragment : PreferenceFragmentCompat() {

    lateinit var settingsViewModel: SettingsViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        val prefDiet = findPreference<ListPreference>("diet")
        val prefIntolerance = findPreference<MultiSelectListPreference>("intolerance")
        val prefDarkMode = findPreference<SwitchPreferenceCompat>("dark_mode")

        prefDiet?.setOnPreferenceChangeListener { _, newValue ->
            settingsViewModel.setDiet(newValue as String)
            Toast.makeText(activity, "Diet: $newValue", Toast.LENGTH_LONG).show()

            Log.d("Diet", newValue)
            true
        }

        prefIntolerance?.setOnPreferenceChangeListener { preference, newValue ->

            settingsViewModel.setIntolerances(newValue as HashSet<String>)
            val selected = newValue.toString()
                .replace("[", "")
                .replace("]", "")

            preference.summary = if (selected.isNotBlank()) selected else "None Selected"

            Toast.makeText(activity, "Intolerances: $selected", Toast.LENGTH_LONG)
                .show()

            Log.d("Intolerance", selected)
            true
        }



        prefDarkMode?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == true) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Toast.makeText(activity, "Dark Mode Enabled", Toast.LENGTH_LONG).show()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Toast.makeText(activity, "Dark Mode Disabled", Toast.LENGTH_LONG).show()
            }
            Log.d("Dark Mode", newValue.toString())
            true
        }
    }
}
