package com.example.cookbook.ui.fragment

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.MultiSelectListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.cookbook.R
import com.example.cookbook.ui.viewModel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    lateinit var viewModel: SharedViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val prefDiet = findPreference<ListPreference>("diet")
        val prefIntolerance = findPreference<MultiSelectListPreference>("intolerance")
        val prefDarkMode = findPreference<SwitchPreferenceCompat>("dark_mode")

        prefDiet?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setDiet(newValue as String)
            Toast.makeText(activity, "Diet: $newValue", Toast.LENGTH_LONG).show()
            true
        }

        prefIntolerance?.setOnPreferenceChangeListener { preference, newValue ->

            if (newValue is HashSet<*>) {
                viewModel.setIntolerances(newValue.filterIsInstance<String>() as ArrayList<String>)
            }
            
            val selected = newValue.toString()
                .replace("[", "")
                .replace("]", "")

            preference.summary = if (selected.isNotBlank()) selected else "None Selected"

            Toast.makeText(activity, "Intolerances: $selected", Toast.LENGTH_LONG).show()
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