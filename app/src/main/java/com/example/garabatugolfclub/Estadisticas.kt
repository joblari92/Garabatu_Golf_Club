package com.example.garabatugolfclub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.garabatugolfclub.databinding.ActivityEstadisticasBinding
import com.example.garabatugolfclub.databinding.ActivityStatsCamposBinding

class Estadisticas : AppCompatActivity() {

    /*-----------------------------------------Variables------------------------------------------*/

    private lateinit var binding: ActivityEstadisticasBinding

    /*------------------------------------------onCreate------------------------------------------*/

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)
        binding = ActivityEstadisticasBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        //Botón campos
        binding.botonCampos.setOnClickListener{
            val i = Intent(this,StatsCampos::class.java)
            startActivity(i)
        }

        //Botón palos
        binding.botonPalos.setOnClickListener{
            val i = Intent(this,StatsPalos::class.java)
            startActivity(i)
        }

        //---------------------------Funciones barra de menú----------------------------------------

        //Botón Inicio
        binding.botonHome.setOnClickListener {
            val i = Intent(this, Inicio::class.java)
            startActivity(i)
        }
        //Botón Historial
        binding.botonHistorial.setOnClickListener {
            val i = Intent(this, Historial::class.java)
            startActivity(i)
        }
    }
}