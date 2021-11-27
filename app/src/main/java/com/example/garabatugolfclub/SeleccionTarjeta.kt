package com.example.garabatugolfclub

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.example.garabatugolfclub.databinding.ActivitySeleccionTarjetaBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList
import com.google.firebase.firestore.QueryDocumentSnapshot





class SeleccionTarjeta : AppCompatActivity() {
    private lateinit var binding: ActivitySeleccionTarjetaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)

        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionTarjetaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val db = Firebase.firestore

        db.collection("campos").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val list: MutableList<String> =
                    ArrayList()
                for (document in task.result) {
                    list.add(document.id)
                }
                Log.d("listCampos", list.toString())
            } else {
                Log.d("listCampos", "Error getting documents: ", task.exception)
            }
        }


    }


}