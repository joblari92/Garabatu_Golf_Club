package com.example.garabatugolfclub

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
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
    var par = ""
    var handicap = ""

    fun crearPartido(campo:String,handicap:String){
        db.collection("usuarios").document(email)
            .collection("partidos").document(id)
            .set(hashMapOf("campo" to campo, "handicap" to handicap,
            "fecha" to currentDate,
            "hora" to hora,
            "golpes 1" to "","golpes 2" to "","golpes 3" to "","golpes 4" to "","golpes 5" to ""
                ,"golpes 6" to "","golpes 7" to "","golpes 8" to "","golpes 9" to ""
                ,"golpes 10" to "","golpes 11" to "","golpes 12" to "","golpes 13" to ""
                ,"golpes 14" to "","golpes 15" to "","golpes 16" to "","golpes 17" to ""
                ,"golpes 18" to "",))
    }

    fun getIdPartido(): String {
        return id
    }

    fun setGolpe(golpes:String,hoyo:String,id:String){
        var golpesHoyo: String = "golpes " + hoyo
        db.collection("usuarios").document(email)
            .collection("partidos").document(id)
            .update(golpesHoyo, golpes)
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }

    }


}