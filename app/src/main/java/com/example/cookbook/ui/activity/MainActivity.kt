package com.example.cookbook.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.cookbook.R
import com.example.cookbook.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, true)

       if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_mode", true)) {
           AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
       } else {
           AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
       }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar
        val bottomNavigation: BottomNavigationView = binding.navView
        toolbarConfiguration = AppBarConfiguration(navController.graph)
        val bottomNavBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_cookbook,
                R.id.navigation_favorites,
                R.id.navigation_shopping_list,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, toolbarConfiguration)
        setupActionBarWithNavController(navController, bottomNavBarConfiguration)

        bottomNavigation.setupWithNavController(navController)
        toolbar.setupWithNavController(navController)

        if (savedInstanceState == null) {
            navController.navigateUp()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d("EVENT", "BACK PRESSED")
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(toolbarConfiguration)
                || super.onSupportNavigateUp()
    }
}