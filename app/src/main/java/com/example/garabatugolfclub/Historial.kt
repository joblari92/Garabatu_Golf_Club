package com.example.garabatugolfclub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.keystore.StrongBoxUnavailableException
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.garabatugolfclub.databinding.ActivityEstadisticasBinding
import com.example.garabatugolfclub.databinding.ActivityHistorialBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class Historial : AppCompatActivity() {

    /*------------------------------------Variables-----------------------------------------------*/

    private lateinit var binding: ActivityHistorialBinding
    val usuario = Firebase.auth.currentUser?.email.toString()
    val db = FirebaseFirestore.getInstance()
    var listaPartidos: MutableList<Tarjetas> = mutableListOf()

    /*------------------------------------onCreate------------------------------------------------*/

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)
        binding = ActivityHistorialBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)


            recuperarInformacion()

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

    /*---------------------------------------Funciones--------------------------------------------*/

    /*Recuperamos los valores de cada partido que vamos a mostrar en las tarjetas, asegurándonos
    * de que ninguno esté vacío*/
    fun recuperarInformacion() {
        db.collection("usuarios").document(usuario).collection("partidos")
            .get().addOnSuccessListener { documents ->
                for (document in documents) {
                    try {
                        val campo = document.get("campo") as String
                        val puntos = document.get("resultado") as String
                        val fecha = document.get("fecha") as String
                        val hora = document.get("hora") as String
                        if (campo.isNotEmpty() && puntos.isNotEmpty() && fecha.isNotEmpty() &&
                            hora.isNotEmpty()
                        ) {
                            listaPartidos.add(Tarjetas(campo, puntos, fecha, hora))
                            Log.d("Datos", listaPartidos.toString())
                            initRecycler()
                        } else {
                            Log.d("Datos", "Datos incompletos")
                        }
                    }catch (e: Exception){
                        Log.d("Datos", "Datos incompletos")
                    }

                }
            }

    }

    /*Inicializamos el recyclerView*/
    fun initRecycler(){
        try {

            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            val adapter = TarjetaAdapter(listaPartidos)
            binding.recyclerView.adapter = adapter
            Log.d("Datos", "EXITO RECYCLERVIEW")
        }catch (e: Exception){
            Log.d("Datos", "ERROR RECYCLERVIEW")
        }

    }

}