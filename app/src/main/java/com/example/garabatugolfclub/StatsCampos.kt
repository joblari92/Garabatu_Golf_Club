package com.example.garabatugolfclub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.garabatugolfclub.databinding.ActivityStatsCamposBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.lang.ArithmeticException
import java.lang.Exception

class StatsCampos : AppCompatActivity() {

    /*--------------------------------------Variables---------------------------------------------*/

    private lateinit var binding: ActivityStatsCamposBinding
    val usuario = Firebase.auth.currentUser?.email.toString()
    val db = FirebaseFirestore.getInstance()
    var campo = ""
    var puntuacionMax = 0
    var mediaPuntuacion = 0
    var totalDoc = 0

    /*--------------------------------------onCreate----------------------------------------------*/

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)

        super.onCreate(savedInstanceState)
        binding = ActivityStatsCamposBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Desplegable en el que seleccionaremos el campo

        val campos = resources.getStringArray(R.array.campos)
        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item,campos)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        //Cuando seleccionamos un campo del desplegable recuperamos los datos de Firestore

        binding.autoCompleteTextView.addTextChangedListener {
            campo = binding.autoCompleteTextView.text.toString()
            if (campo.isNotEmpty()){
                recuperarInformacion(campo)
            }else{
                Toast.makeText(baseContext,"Seleccione un campo",Toast.LENGTH_SHORT).show()
            }
        }

        //Actualizamos los datos del layout

        binding.botonActualizar.setOnClickListener {
            if (puntuacionMax != 0 && mediaPuntuacion != 0){
                binding.totalPartidos.setText(totalDoc.toString())
                binding.puntuacionMedia.setText(mediaPuntuacion.toString())
                binding.maxPuntuacion.setText(puntuacionMax.toString())
            }else{
                Toast.makeText(baseContext,"Sin datos",Toast.LENGTH_SHORT).show()
                binding.maxPuntuacion.setText("0")
                binding.totalPartidos.setText("0")
                binding.puntuacionMedia.setText("0")
            }
        }

        //Regresamos al menú de estadísticas

        binding.botonSalir.setOnClickListener {
            val i = Intent(this, Estadisticas::class.java)
            startActivity(i)
        }
    }

    /*-------------------------------------Funciones----------------------------------------------*/

    fun recuperarInformacion(campo: String) {
        var listaDocCampo = mutableListOf<String>()
        var listaPuntosCampo = mutableListOf<Int>()
        puntuacionMax = 0
        mediaPuntuacion = 0
        totalDoc = 0
        db.collection("usuarios").document(usuario).collection("partidos")
            .whereEqualTo("campo",campo).get().addOnSuccessListener { documents ->
                for (document in documents) {
                    listaDocCampo.add(document.toString())//Añadimos cada documento que contiene
                    //el campo seleccionado
                    try {
                        listaPuntosCampo.add((document.get("resultado") as String).toInt())//Añadimos los
                        //puntos de cada partido en el campo seleccionado
                        totalDoc ++
                    }catch(e: Exception){
                        Log.d("sinPuntos", "Puntuación no encontrada")
                    }
                }
                var sumaPuntos = 0
                var i = 0
                //Con el bucle while obtenemos la mayor puntuación obtenida
                while (i<listaPuntosCampo.size){
                    if (puntuacionMax<=listaPuntosCampo.get(i)){
                        puntuacionMax = listaPuntosCampo.get(i)
                    }
                    sumaPuntos += listaPuntosCampo.get(i)
                    i++
                }
                //Calculamos la puntuación media
                if (totalDoc != 0){
                    mediaPuntuacion = sumaPuntos / totalDoc
                }
            }
    }
}