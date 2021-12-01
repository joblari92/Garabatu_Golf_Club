package com.example.garabatugolfclub

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.garabatugolfclub.databinding.ActivityTarjetaAvanzadaBinding

class TarjetaAvanzada : AppCompatActivity() {
    private lateinit var binding: ActivityTarjetaAvanzadaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)

        super.onCreate(savedInstanceState)
        binding = ActivityTarjetaAvanzadaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val startPoint = Location("locationA")
        startPoint.setLatitude(17.372102)
        startPoint.setLongitude(78.484196)

        val endPoint = Location("locationA")
        endPoint.setLatitude(17.375775)
        endPoint.setLongitude(78.469218)

        val distance: Float = startPoint.distanceTo(endPoint)

        binding.distancia.setText(distance.toString())

    }

}