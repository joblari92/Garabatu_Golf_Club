package com.example.garabatugolfclub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.garabatugolfclub.databinding.ActivityStatsPalosBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class StatsPalos : AppCompatActivity() {

    /*-------------------------------------Variables----------------------------------------------*/

    private lateinit var binding: ActivityStatsPalosBinding
    val usuario = Firebase.auth.currentUser?.email.toString()
    val db = FirebaseFirestore.getInstance()
    var palo = ""
    var distMaxima = -1
    var listaDocPalo = mutableListOf<String>()
    var totalDoc = 0
    var mediaDistancias = 0
    var distanciaMax = 0
    var listaDocLlorea = mutableListOf<String>()
    var listaDocLlanes = mutableListOf<String>()
    var listaDocNestares = mutableListOf<String>()
    var totalLlorea = 0
    var totalNestares = 0
    var totalLlanes = 0

    /*---------------------------------------onCreate---------------------------------------------*/

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

        //Cuando escogemos un palo de la lista recuperamos los datos de Firestore

        binding.autoCompleteTextView.addTextChangedListener {
            palo = binding.autoCompleteTextView.text.toString()
            if (palo.isNotEmpty()){
                totalDocPalo(palo)
            }else{
                Toast.makeText(baseContext,"Seleccione un palo",Toast.LENGTH_SHORT).show()
            }
        }

        //Actualizamos los datos del layout

        binding.botonActualizar.setOnClickListener {
            if (totalLlorea>totalLlanes && totalLlorea>totalNestares){
                binding.campo.setText(totalLlorea.toString())
            }else if(totalLlanes>totalLlorea && totalLlanes>totalNestares){
                binding.campo.setText(totalLlanes.toString())
            }else if(totalNestares>totalLlanes && totalNestares>totalLlorea){
                binding.campo.setText(totalNestares.toString())
            }else{
                binding.campo.setText("Empatados")
            }
            binding.maxDistancia.setText(distanciaMax.toString())
            binding.distanciaMedia.setText(mediaDistancias.toString())
        }

        //Regresamos al menú de estadísiticas

        binding.botonSalir.setOnClickListener {
            val i = Intent(this,Estadisticas::class.java)
            startActivity(i)
        }
    }

    /*-------Añadimos a una lista todos los documentos que contienen el palo seleccionado---------*/

    fun totalDocPalo(palo:String){
        var listaDocPalo = mutableListOf<String>()
        var listaDistPalo = mutableListOf<Int>()
        var listaDocLlorea = mutableListOf<String>()
        var listaDocLlanes = mutableListOf<String>()
        var listaDocNestares = mutableListOf<String>()
        distanciaMax = 0
        mediaDistancias = 0
        db.collection("usuarios").document(usuario).collection("golpes")
            .whereEqualTo("palo",palo).get().addOnSuccessListener { documents ->
                for (document in documents) {
                    listaDocPalo.add(document.toString())
                    listaDistPalo.add((document.get("distancia") as String).toInt())
                }
                totalDoc = listaDocPalo.size
                var sumaDisntacias = 0
                var i = 0
                while (i<listaDistPalo.size){
                    if (distanciaMax <= listaDistPalo.get(i)){
                        distanciaMax = listaDistPalo.get(i)
                    }
                    sumaDisntacias += listaDistPalo.get(i)
                    i++
                }
                mediaDistancias = sumaDisntacias/totalDoc


            }
        db.collection("usuarios").document(usuario).collection("golpes")
            .whereEqualTo("palo",palo).whereEqualTo("campo","La Llorea").get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    listaDocLlorea.add(document.toString())
                }
                totalLlorea = listaDocLlorea.size

            }
        db.collection("usuarios").document(usuario).collection("golpes")
            .whereEqualTo("palo",palo).whereEqualTo("campo","Nestares").get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    listaDocNestares.add(document.toString())
                }
                totalNestares = listaDocNestares.size

            }
        db.collection("usuarios").document(usuario).collection("golpes")
            .whereEqualTo("palo",palo).whereEqualTo("campo","Llanes").get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    listaDocLlanes.add(document.toString())
                }
                totalLlanes = listaDocLlanes.size

            }

    }
}
