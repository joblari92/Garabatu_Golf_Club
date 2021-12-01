package com.example.garabatugolfclub

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Golpes(val email: String) {

    private val db = FirebaseFirestore.getInstance() //Instancia de la base de datos

    fun setGolpe(campo: String, palo: String, distancia: String){
        db.collection("usuarios").document(email)
            .collection("golpes").document().set(hashMapOf(
                "campo" to campo, "palo" to palo, "distancia" to distancia)
            )
    }
}