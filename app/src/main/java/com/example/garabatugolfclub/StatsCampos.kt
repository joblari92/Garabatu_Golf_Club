package com.example.garabatugolfclub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.garabatugolfclub.databinding.ActivityStatsCamposBinding

class StatsCampos : AppCompatActivity() {
    private lateinit var binding: ActivityStatsCamposBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)

        super.onCreate(savedInstanceState)
        binding = ActivityStatsCamposBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Desplegable en el que seleccionaremos el campo
        val campos = resources.getStringArray(R.array.campos)
        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item,campos)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }
}