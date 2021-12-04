package com.example.garabatugolfclub

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import com.example.garabatugolfclub.databinding.ActivityInicioBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.transition.MaterialArcMotion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Inicio : AppCompatActivity() {
    private lateinit var binding: ActivityInicioBinding

    /*Instanciamos la clase usuario*/
    val usuario = Usuario(Firebase.auth.currentUser?.email.toString())
    val campos = Campos(Firebase.auth.currentUser?.email.toString())


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)

        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.botonUsuario.setOnClickListener {

            PopupUsuario()

        }

        binding.botonNuevaPartida.setOnClickListener {

            val i = Intent(this, SeleccionTarjeta::class.java)
            startActivity(i)

        }

        //Resgistro de usuario en la BBDD
        if (Firebase.auth.currentUser != null) {
            Log.d("existeUsuario", "Usuario en uso " + Firebase.auth.currentUser?.email)
            usuario.registrarUsuario()
            //Registro de campos para el usuario actual
            campos.crearCampos()
        }

        //---------------------------Funciones barra de menú----------------------------------------


        //Botón Historial
        binding.botonHistorial.setOnClickListener {
            val i = Intent(this, Historial::class.java)
            startActivity(i)
        }
        //Botón Stats
        binding.botonStats.setOnClickListener {
            val i = Intent(this, Estadisticas::class.java)
            startActivity(i)
        }

    }



    override fun onDestroy() {

        super.onDestroy()
    }

    /*Popup para mostrar el usuario actual y cerrar la sesión*/
    private fun PopupUsuario(){

        val popupUsuario = Dialog(this)
        popupUsuario.setContentView(R.layout.popup_usuario)
        popupUsuario.show()
        val cerrarSesion = popupUsuario.findViewById<Button>(R.id.cerrarSesion)
        val mailusuario = popupUsuario.findViewById<TextView>(R.id.usuarioPopup)
        mailusuario.setText(Firebase.auth.currentUser?.email)
        cerrarSesion.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }

    }

}

