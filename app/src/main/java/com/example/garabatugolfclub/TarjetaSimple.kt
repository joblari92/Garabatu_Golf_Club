package com.example.garabatugolfclub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.garabatugolfclub.databinding.ActivitySeleccionTarjetaBinding
import com.example.garabatugolfclub.databinding.ActivityTarjetaSimpleBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class TarjetaSimple : AppCompatActivity() {
    private lateinit var binding: ActivityTarjetaSimpleBinding

    //Variables
    val usuario = Firebase.auth.currentUser?.email.toString()
    val db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)

        super.onCreate(savedInstanceState)
        binding = ActivityTarjetaSimpleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val campoSeleccionado = intent.getStringExtra("campoSeleccionado")//Recuperamos nombre del campo
        // de la activity anterior
        val idPartido = intent.getStringExtra("idPartido") //Recuperamos el ID del partido que se está
        //jugando para almacenar los resultados

        binding.nombreCampo.setText(campoSeleccionado) //Indicamos el nombre del campo

        //PROVISIONAL recuperamos el par y handicap del hoyo 1 en el campo que seleccionamos
        if (idPartido != null && campoSeleccionado != null) {
            Log.d("tarjeta", "El if se inicia")
            db.collection("usuarios").document(usuario)
                .collection("campos").document(campoSeleccionado).get().addOnSuccessListener {
                    binding.handicapHoyo.setText(it.get("hcap 1") as String?)
                    binding.parHoyo.setText(it.get("par 1") as String?)
                }
        }



    }
}