package com.example.cookbook.ui.cookbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cookbook.R

class CookBookFragment : Fragment() {

    private lateinit var cookBookViewModel: CookBookViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        cookBookViewModel =
                ViewModelProvider(this).get(CookBookViewModel::class.java)
        //val root = inflater.inflate(R.layout.fragment_cookbook, container, false)
        //val textView: TextView = root.findViewById(R.id.text_cookbook)
        //cookBookViewModel.text.observe(viewLifecycleOwner, Observer {
        //    textView.text = it
        //})
        return inflater.inflate(R.layout.fragment_cookbook, container, false) //root
    }
}