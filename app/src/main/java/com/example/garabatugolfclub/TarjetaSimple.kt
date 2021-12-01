package com.example.garabatugolfclub

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.garabatugolfclub.databinding.ActivityTarjetaSimpleBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class TarjetaSimple : AppCompatActivity() {
    private lateinit var binding: ActivityTarjetaSimpleBinding

    //Variable para evitar salir de la activity directamente
    private var backPressedTime = 0L

    //Variables
    val usuario = Firebase.auth.currentUser?.email.toString()
    val db = FirebaseFirestore.getInstance()
    val partido = Partidos(usuario)
    var hoyo = 1
    var puntos = 0

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

                            var par = binding.parHoyo.text.toString().toInt()
                            var golpes = binding.golpes.text.toString().toInt()
                            puntos(par, golpes)
                            if(hoyo == 18){//Cuando lleguemos al último hoyo pasamos al resumen del partido
                                partido.setResultado(puntos.toString(),idPartido)
                                val i = Intent(this, ResumenTarjeta::class.java)
                                i.putExtra("campoSeleccionado",campoSeleccionado)
                                i.putExtra("idPartido",idPartido)
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

        /*Al pulsar botón cancelar
    * volveremos a la activity SeleccionTarjeta y eliminaremos los datos del partido
    * que estábamos jugando, para no tener registros incompletos en la base de datos.*/
        binding.botonCancelar.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("SALIR")
            builder.setMessage("¿Seguro que desea salir del partido?")
            builder.setPositiveButton("ACEPTAR",DialogInterface.OnClickListener{
                    dialog,which->
                if (idPartido != null) {
                    db.collection("usuarios").document(usuario)
                        .collection("partidos").document(idPartido).delete()
                }
                val i = Intent(this, SeleccionTarjeta::class.java)
                startActivity(i)

            })
            builder.setNegativeButton("CANCELAR",null)
            builder.show()
        }

    }

    /*Al pulsar dos veces seguidas en menos de dos segundos el botón atrás
    * volveremos a la activity de selección de pantalla y eliminaremos los datos del partido
    * que estábamos jugando, para no tener registros incompletos en la base de datos.*/
    override fun onBackPressed() {
        val idPressedBack = intent.getStringExtra("idPartido")
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed()
            if (idPressedBack != null) {
                db.collection("usuarios").document(usuario)
                    .collection("partidos").document(idPressedBack).delete()
            }
        }else{
            Toast.makeText(this,"Pulsa atrás otra vez para salir", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    //Función para asignar los puntos a cada hoyo
    fun puntos(par:Int,golpes:Int){
        if (par - golpes == 0){
            puntos += 2
        }else if (par - golpes == 1){
            puntos += 3
        }else if(par - golpes == 2){
            puntos += 4
        }else if(par - golpes == 3){
            puntos += 5
        }else if (par - golpes == 4){
            puntos += 6
        }else if (par - golpes == -1) {
            puntos += 1
        }
    }

}