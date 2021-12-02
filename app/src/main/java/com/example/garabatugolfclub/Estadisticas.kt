package com.example.garabatugolfclub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.garabatugolfclub.databinding.ActivityEstadisticasBinding
import com.example.garabatugolfclub.databinding.ActivityStatsCamposBinding

class Estadisticas : AppCompatActivity() {
    private lateinit var binding: ActivityEstadisticasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)
        binding = ActivityEstadisticasBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
    }
}