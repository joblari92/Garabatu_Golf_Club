package com.example.garabatugolfclub

import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Partidos(val email: String) {

    private val db = FirebaseFirestore.getInstance() //Instancia de la base de datos

    val date = Calendar.getInstance()
    val currentDate = DateFormat.getDateInstance().format(date.time) //obtenemos la fecha actual
    val simpleDateFormat = SimpleDateFormat("hh:mm:ss")
    val hora = simpleDateFormat.format(date.time) //obtenemos la hora actual
    val id = currentDate + hora

    fun crearPartido(campo:String,handicap:String){
        db.collection("usuarios").document(email)
            .collection("partidos").document(id)
            .set(hashMapOf("campo" to campo, "handicap" to handicap,
            "fecha" to currentDate,
            "hora" to hora))
    }

    fun getIdPartido(): String {
        return id
    }


}