package com.example.garabatugolfclub

import com.google.common.collect.ListMultimap
import com.google.firebase.firestore.FirebaseFirestore

class Campos(val email:String) {

    private val db = FirebaseFirestore.getInstance() //Instancia de la base de datos
    private val campos: MutableList<String> = mutableListOf("La Llorea","Nestares","Llanes")

    fun crearCampos(){ //Introduce en la BBDD los valores predeterminados de los campos
        db.collection("usuarios").document(email)
            .collection("campos").document("La Llorea")
            .set(hashMapOf("par 1" to "5","par 2" to "4","par 3" to "4","par 4" to "4",
                "par 5" to "3","par 6" to "5","par 7" to "4","par 8" to "3","par 9" to "4",
                "par 10" to "5","par 11" to "4", "par 12" to "4","par 13" to "3","par 14" to "4",
                "par 15" to "5","par 16" to "3","par 17" to "4","par 18" to "4", "hcap 1" to "7",
                "hcap 2" to "9","hcap 3" to "15","hcap 4" to "1","hcap 5" to "17","hcap 6" to "3",
                "hcap 7" to "5","hcap 8" to "13","hcap 9" to "11","hcap 10" to "4","hcap 11" to "10",
                "hcap 12" to "2","hcap 13" to "8","hcap 14" to "14","hcap 15" to "16","hcap 16" to "6",
                "hcap 17" to "18","hcap 18" to "12"))

        db.collection("usuarios").document(email)
            .collection("campos").document("Nestares")
            .set(hashMapOf("par 1" to "5","par 2" to "3","par 3" to "4","par 4" to "4",
                "par 5" to "3","par 6" to "4","par 7" to "4","par 8" to "5","par 9" to "4",
                "par 10" to "3","par 11" to "4", "par 12" to "4","par 13" to "5","par 14" to "4",
                "par 15" to "3","par 16" to "4","par 17" to "4","par 18" to "5", "hcap 1" to "1",
                "hcap 2" to "15","hcap 3" to "4","hcap 4" to "2","hcap 5" to "5","hcap 6" to "13",
                "hcap 7" to "6","hcap 8" to "7","hcap 9" to "8","hcap 10" to "17","hcap 11" to "18",
                "hcap 12" to "3","hcap 13" to "9","hcap 14" to "10","hcap 15" to "16","hcap 16" to "14",
                "hcap 17" to "11","hcap 18" to "12"))

        db.collection("usuarios").document(email)
            .collection("campos").document("Llanes")
            .set(hashMapOf("par 1" to "5","par 2" to "3","par 3" to "4","par 4" to "4",
                "par 5" to "4","par 6" to "5","par 7" to "3","par 8" to "5","par 9" to "3",
                "par 10" to "4","par 11" to "5", "par 12" to "3","par 13" to "4","par 14" to "4",
                "par 15" to "3","par 16" to "4","par 17" to "4","par 18" to "5", "hcap 1" to "5",
                "hcap 2" to "18","hcap 3" to "2","hcap 4" to "1","hcap 5" to "16","hcap 6" to "13",
                "hcap 7" to "10","hcap 8" to "17","hcap 9" to "6","hcap 10" to "15","hcap 11" to "11",
                "hcap 12" to "12","hcap 13" to "4","hcap 14" to "14","hcap 15" to "9","hcap 16" to "3",
                "hcap 17" to "8","hcap 18" to "7"))



    }

    fun getCampos(): MutableList<String> {
        return campos
    }

}