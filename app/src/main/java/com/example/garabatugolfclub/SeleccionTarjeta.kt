package com.example.garabatugolfclub

import android.app.Dialog
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

        //Desplegable en el que seleccionaremos el campo en el que vamos a jugar
        val campos = resources.getStringArray(R.array.campos)
        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item,campos)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        //Variable para almacenar el campo y poder conseguir sus datos de la BBDD
        val campoSeleccionado = binding.autoCompleteTextView.text


    }


}