package com.example.garabatugolfclub

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.example.garabatugolfclub.databinding.ActivitySeleccionTarjetaBinding
import com.example.garabatugolfclub.databinding.ActivityTarjetaSimpleBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.protobuf.Empty

class TarjetaSimple : AppCompatActivity() {
    private lateinit var binding: ActivityTarjetaSimpleBinding

    //Variables
    val usuario = Firebase.auth.currentUser?.email.toString()
    val db = FirebaseFirestore.getInstance()
    val partido = Partidos(usuario)
    var hoyo = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)

        super.onCreate(savedInstanceState)
        binding = ActivityTarjetaSimpleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val campoSeleccionado = intent.getStringExtra("campoSeleccionado")//Recuperamos nombre del campo
        // de la activity anterior
        val idPartido = intent.getStringExtra("idPartido") //Recuperamos el ID del partido que se está
        //jugando para almacenar los resultados

        binding.nombreCampo.setText(campoSeleccionado) //Indicamos el nombre del campo
        binding.golpes.setText(null)


        //Recuperamos el par y handicap del hoyo 1 en el campo que seleccionamos
        if (idPartido != null && campoSeleccionado != null) {
            db.collection("usuarios").document(usuario)
                .collection("campos").document(campoSeleccionado).get().addOnSuccessListener {
                    binding.handicapHoyo.setText(it.get("hcap " + hoyo) as String?)
                    binding.parHoyo.setText(it.get("par " + hoyo) as String?)
                }
        }


        binding.botonSiguiente.setOnClickListener {
            if(binding.golpes.text.isNotEmpty()) {
                if (hoyo <= 18) {
                    if (idPartido != null && campoSeleccionado != null) {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("SIGUIENTE HOYO")
                        builder.setMessage("¿Continuar al siguiente hoyo?")
                        builder.setPositiveButton("ACEPTAR",DialogInterface.OnClickListener{
                            dialog,which->
                            db.collection("usuarios").document(usuario)
                                .collection("campos").document(campoSeleccionado).get()
                                .addOnSuccessListener {
                                    if (hoyo <=18) {
                                        binding.handicapHoyo.setText(it.get("hcap " + hoyo) as String?) //Cambiamos el handicap del hoyo
                                        binding.parHoyo.setText(it.get("par " + hoyo) as String?) //Cambiamos el par del hoyo
                                        binding.numHoyo.setText(hoyo.toString()) //Cambiamos el hoyo
                                    }
                                }
                                partido.setGolpe(
                                    binding.golpes.text.toString(),
                                    hoyo.toString(),
                                    idPartido.toString()
                                ) //Guardamos el número de golpes
                            if(hoyo == 18){//Cuando lleguemos al último hoyo pasamos al resumen del partido
                                val i = Intent(this, ResumenTarjeta::class.java)
                                startActivity(i)
                            }else {
                                hoyo++
                                binding.golpes.setText(null)
                            }
                        })
                        builder.setNegativeButton("CANCELAR",null)
                        builder.show()
                    }
                }
            }else {
                Toast.makeText(this,"Introduzca los golpes",Toast.LENGTH_SHORT).show()
            }
        }

        binding.botonCancelar.setOnClickListener {
            TODO("Salir del partido en curso y eliminar el documento de la BBDD. Utilizar y AlertDialog" +
                    "para confirmar antes de salir")
        }



    }

}