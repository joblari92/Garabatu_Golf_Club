package com.example.garabatugolfclub

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class Usuario(val email:String) {
    private var handicap = 36

    private val db = FirebaseFirestore.getInstance() //Instancia de la base de datos

    fun registrarUsuario(){ //Crea un nuevo documento para cada usuario registrado en la colección usuarios
        db.collection("usuarios").document(email).set(
            hashMapOf("handicap" to handicap.toString())
        )

    }

    fun setHandicap(hcap: Int){
        handicap = hcap
    }

    fun getHandicap(): Int {
        return handicap
    }

}