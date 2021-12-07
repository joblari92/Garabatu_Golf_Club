package com.example.garabatugolfclub

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.garabatugolfclub.databinding.ActivityResumenTarjetaBinding
import com.example.garabatugolfclub.databinding.ActivityTarjetaSimpleBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ResumenTarjeta : AppCompatActivity() {

    /*--------------------------------------Variables---------------------------------------------*/

    private lateinit var binding: ActivityResumenTarjetaBinding
    val usuario = Firebase.auth.currentUser?.email.toString()
    val db = FirebaseFirestore.getInstance()

    //Variable para evitar salir de la activity directamente
    private var backPressedTime = 0L

    /*--------------------------------------onCreate----------------------------------------------*/

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)

        super.onCreate(savedInstanceState)
        binding = ActivityResumenTarjetaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /*Recuperamos las variables enviadas desde la activity anterior*/
        val campoSeleccionado = intent.getStringExtra("campoSeleccionado")
        val idPartido = intent.getStringExtra("idPartido")

        binding.campoResumen.setText(campoSeleccionado)

        /*Recuperamos los golpes dados en cada hoyo del recorrido*/
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos1.setText(it.get("golpes 1") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos2.setText(it.get("golpes 2") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos3.setText(it.get("golpes 3") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos4.setText(it.get("golpes 4") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos5.setText(it.get("golpes 5") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos6.setText(it.get("golpes 6") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos7.setText(it.get("golpes 7") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos8.setText(it.get("golpes 8") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos9.setText(it.get("golpes 9") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos10.setText(it.get("golpes 10") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos11.setText(it.get("golpes 11") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos12.setText(it.get("golpes 12") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos13.setText(it.get("golpes 13") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos14.setText(it.get("golpes 14") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos15.setText(it.get("golpes 15") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos16.setText(it.get("golpes 16") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos17.setText(it.get("golpes 17") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos18.setText(it.get("golpes 18") as String? + " golpes")
                }
        }
        if (idPartido != null) {
            db.collection("usuarios").document(usuario)
                .collection("partidos").document(idPartido).get()
                .addOnSuccessListener {
                    binding.puntos.setText(it.get("resultado") as String?)
                }
        }
        binding.botonCerrarResumen.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("SALIR")
                builder.setMessage("¿Desea cerrar el resumen?")
                builder.setPositiveButton("ACEPTAR", DialogInterface.OnClickListener{
                        dialog,which->
                    val i = Intent(this, Inicio::class.java)
                    startActivity(i)

                })
                builder.setNegativeButton("CANCELAR",null)
                builder.show()
        }

    }
    override fun onBackPressed() {
        val idPressedBack = intent.getStringExtra("idPartido")
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed()
            if (idPressedBack != null) {
                val i = Intent(this, Inicio::class.java)
                startActivity(i)
            }
        }else{
            Toast.makeText(this,"Pulsa atrás otra vez para salir", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}