package com.example.garabatugolfclub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.garabatugolfclub.databinding.ActivityStatsPalosBinding

class StatsPalos : AppCompatActivity() {
    private lateinit var binding: ActivityStatsPalosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)
        super.onCreate(savedInstanceState)
        binding = ActivityStatsPalosBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}