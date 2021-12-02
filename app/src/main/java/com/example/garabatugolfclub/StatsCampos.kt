package com.example.garabatugolfclub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.garabatugolfclub.databinding.ActivityStatsCamposBinding

class StatsCampos : AppCompatActivity() {
    private lateinit var binding: ActivityStatsCamposBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)

        super.onCreate(savedInstanceState)
        binding = ActivityStatsCamposBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}