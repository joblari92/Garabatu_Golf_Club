package com.example.garabatugolfclub

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.io.path.createTempDirectory

class Partidos(val email: String) {

    private val db = FirebaseFirestore.getInstance() //Instancia de la base de datos

    val date = Calendar.getInstance()
    val currentDate = DateFormat.getDateInstance().format(date.time) //obtenemos la fecha actual
    val simpleDateFormat = SimpleDateFormat("hh:mm:ss")
    val hora = simpleDateFormat.format(date.time) //obtenemos la hora actual
    val id = currentDate + hora
    var puntos = 0


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
                ,"golpes 18" to "", "resultado" to ""))
    }

    fun getIdPartido(): String {
        return id
    }

    //Almacenamos cada golpe dado
    fun setGolpe(golpes:String,hoyo:String,id:String){
        var golpesHoyo: String = "golpes " + hoyo
        db.collection("usuarios").document(email)
            .collection("partidos").document(id)
            .update(golpesHoyo, golpes)
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }

    }

    //Almacenamos el total de puntos obtenidos
    fun setResultado(puntos:String,id:String){
        db.collection("usuarios").document(email)
            .collection("partidos").document(id)
            .update("resultado",puntos)
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }
    }

    @JvmName("getPuntos1")
    fun getPuntos(): Int{
        return puntos
    }

    fun puntos(par:Int,golpes:Int,hcpHoyo:Int,handicap:Int,Puntos:Int) {
        puntos = Puntos
        var gNetos: Int
        if (handicap <= 18) {
            if (hcpHoyo <= handicap) {
                gNetos = golpes - 1
                if (par - gNetos == 0) {
                    puntos += 2
                } else if (par - gNetos == 1) {
                    puntos += 3
                } else if (par - gNetos == 2) {
                    puntos += 4
                } else if (par - gNetos == 3) {
                    puntos += 5
                } else if (par - gNetos == 4) {
                    puntos += 6
                } else if (par - gNetos == -1) {
                    puntos += 1
                }
            } else {
                gNetos = golpes
                if (par - gNetos == 0) {
                    puntos += 2
                } else if (par - gNetos == 1) {
                    puntos += 3
                } else if (par - gNetos == 2) {
                    puntos += 4
                } else if (par - gNetos == 3) {
                    puntos += 5
                } else if (par - gNetos == 4) {
                    puntos += 6
                } else if (par - gNetos == -1) {
                    puntos += 1
                }
            }
        } else {
            if (hcpHoyo <= (handicap - 18)) {
                gNetos = golpes - 2
                if (par - gNetos == 0) {
                    puntos += 2
                } else if (par - gNetos == 1) {
                    puntos += 3
                } else if (par - gNetos == 2) {
                    puntos += 4
                } else if (par - gNetos == 3) {
                    puntos += 5
                } else if (par - gNetos == 4) {
                    puntos += 6
                } else if (par - gNetos == -1) {
                    puntos += 1
                }
            } else {
                gNetos = golpes - 1
                if (par - gNetos == 0) {
                    puntos += 2
                } else if (par - gNetos == 1) {
                    puntos += 3
                } else if (par - gNetos == 2) {
                    puntos += 4
                } else if (par - gNetos == 3) {
                    puntos += 5
                } else if (par - gNetos == 4) {
                    puntos += 6
                } else if (par - gNetos == -1) {
                    puntos += 1
                }
            }
        }
        //Log.d("puntos","puntos " + puntos.toString() + "golpes" + golpes.toString())
    }

}