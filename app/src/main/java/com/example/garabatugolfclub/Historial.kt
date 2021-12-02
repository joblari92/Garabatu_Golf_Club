package com.example.garabatugolfclub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.garabatugolfclub.databinding.ActivityEstadisticasBinding
import com.example.garabatugolfclub.databinding.ActivityHistorialBinding

class Historial : AppCompatActivity() {
    private lateinit var binding: ActivityHistorialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)
        binding = ActivityHistorialBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
    }
}