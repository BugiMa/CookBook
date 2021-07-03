package com.example.cookbook.ui.settings

import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    private var diet: String? = null
    private var intolerances: HashSet<String>? = null
    private var darkMode: Boolean = false

    fun setDiet(diet: String)
    {
        this.diet = diet
    }
    fun getDiet()
    {

    }

    fun setIntolerances(intolerances: HashSet<String>)
    {
        this.intolerances = intolerances
    }
    fun getIntolerances()
    {

    }
}