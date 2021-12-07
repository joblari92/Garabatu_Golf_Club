package com.example.garabatugolfclub

import android.app.Dialog
import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.MenuRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.garabatugolfclub.databinding.ActivitySeleccionTarjetaBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList
import com.google.firebase.firestore.QueryDocumentSnapshot


class SeleccionTarjeta : AppCompatActivity() {

    /*---------------------------------------Variables--------------------------------------------*/

    private lateinit var binding: ActivitySeleccionTarjetaBinding
    val partido = Partidos(Firebase.auth.currentUser?.email.toString())

    /*---------------------------------------onCreate---------------------------------------------*/

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)

        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionTarjetaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Desplegable en el que seleccionaremos el campo en el que vamos a jugar
        val campos = resources.getStringArray(R.array.campos)
        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item,campos)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        /*Selección de tarjeta simple*/
        binding.tarjetaSimple.setOnClickListener {
            val dropdown = binding.autoCompleteTextView.text
            val hcap = binding.handicapPartido.text
            /*Comprobamos que se haya seleccionado un campo y se haya indicado un handicap
            * válido, que debe ser igual o inferior a 36*/
            if(dropdown.isNotEmpty() && hcap.isNotEmpty()) {
                val campoSeleccionado = dropdown.toString()
                val textHandicap = hcap.toString()
                val handicap = textHandicap.toInt()
                if(handicap<=36) {
                    partido.crearPartido(campoSeleccionado, textHandicap)
                    val i = Intent(this, TarjetaSimple::class.java)
                    /*Con estos putExtra enviaremos los datos campo, id y handicap a la
                    * siguiente activity*/
                    i.putExtra("campoSeleccionado", campoSeleccionado)
                    i.putExtra("idPartido", partido.getIdPartido())
                    i.putExtra("handicap", handicap)
                    startActivity(i)
                }else{
                    Toast.makeText(baseContext,"Handicap inválido",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(baseContext,"Datos imcompletos",Toast.LENGTH_SHORT).show()
            }
        }

        /*Selección de tarjeta detallada*/
        binding.tarjetaDetallada.setOnClickListener {
            val dropdown = binding.autoCompleteTextView.text
            val hcap = binding.handicapPartido.text
            /*Comprobamos que se haya seleccionado un campo y se haya indicado un handicap
            * válido, que debe ser igual o inferior a 36*/
            if(dropdown.isNotEmpty() && hcap.isNotEmpty()) {
                val campoSeleccionado = dropdown.toString()
                val textHandicap = hcap.toString()
                val handicap = textHandicap.toInt()
                if(handicap<=36) {
                    partido.crearPartido(campoSeleccionado, textHandicap)
                    val i = Intent(this, TarjetaAvanzada::class.java)
                    /*Con estos putExtra enviaremos los datos campo, id y handicap a la
                    * siguiente activity*/
                    i.putExtra("campoSeleccionado", campoSeleccionado)
                    i.putExtra("idPartido", partido.getIdPartido())
                    i.putExtra("handicap", handicap)
                    startActivity(i)
                }else{
                    Toast.makeText(baseContext,"Handicap inválido",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(baseContext,"Datos imcompletos",Toast.LENGTH_SHORT).show()
            }
        }

        /*---------------------------Funciones barra de menú--------------------------------------*/

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
        //Botón Stats
        binding.botonStats.setOnClickListener {
            val i = Intent(this, Estadisticas::class.java)
            startActivity(i)
        }
    }
}