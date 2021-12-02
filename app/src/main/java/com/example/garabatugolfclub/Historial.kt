package com.example.garabatugolfclub

import android.content.Intent
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

        //---------------------------Funciones barra de menú----------------------------------------

        //Botón Inicio
        binding.botonHome.setOnClickListener {
            val i = Intent(this, Inicio::class.java)
            startActivity(i)
        }
        //Botón Stats
        binding.botonStats.setOnClickListener {
            val i = Intent(this, Estadisticas::class.java)
            startActivity(i)
        }
    }
}