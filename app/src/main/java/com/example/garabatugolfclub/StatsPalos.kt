package com.example.garabatugolfclub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.garabatugolfclub.databinding.ActivityStatsPalosBinding

class StatsPalos : AppCompatActivity() {
    private lateinit var binding: ActivityStatsPalosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)
        super.onCreate(savedInstanceState)
        binding = ActivityStatsPalosBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Desplegable en el que seleccionaremos el palo
        val palos = resources.getStringArray(R.array.palos)
        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item,palos)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }
}