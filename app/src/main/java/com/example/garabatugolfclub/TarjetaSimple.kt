package com.example.garabatugolfclub

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.garabatugolfclub.databinding.ActivityTarjetaSimpleBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class TarjetaSimple : AppCompatActivity() {

    /*----------------------------------------Variables-------------------------------------------*/

    private lateinit var binding: ActivityTarjetaSimpleBinding

    //Variable para evitar salir de la activity directamente
    private var backPressedTime = 0L

    //Variables
    val usuario = Firebase.auth.currentUser?.email.toString()
    val db = FirebaseFirestore.getInstance()
    val partido = Partidos(usuario)
    var hoyo = 1
    var puntos = 0

    /*-----------------------------------------onCreate-------------------------------------------*/

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)

        super.onCreate(savedInstanceState)
        binding = ActivityTarjetaSimpleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /*Recuperamos las variables enviadas desde la activity anterior*/
        val campoSeleccionado = intent.getStringExtra("campoSeleccionado")
        val idPartido = intent.getStringExtra("idPartido")
        val handicap = intent.getIntExtra("handicap",0)

        binding.nombreCampo.setText(campoSeleccionado)
        binding.golpes.setText(null)


        //Recuperamos el par y handicap del hoyo 1 en el campo que seleccionamos
        if (idPartido != null && campoSeleccionado != null) {
            db.collection("usuarios").document(usuario)
                .collection("campos").document(campoSeleccionado).get()
                .addOnSuccessListener {
                    binding.handicapHoyo.setText(it.get("hcap " + hoyo) as String?)
                    binding.parHoyo.setText(it.get("par " + hoyo) as String?)
                }
        }

        //Pasamos de hoyo
        binding.botonSiguiente.setOnClickListener {
            /*Comprobamos que haya golpes registrados*/
            if(binding.golpes.text.isNotEmpty()) {
                /*Comprobamos que todav??a no se haya acabado el recorrido*/
                if (hoyo <= 18) {
                    /*Comprobamos que se haya recuperado la informaci??n de la activity anterior*/
                    if (idPartido != null && campoSeleccionado != null) {
                        /*Creamos un alert que haga que el usuario tenga que confirmar que
                        * quiere pasar de hoyo*/
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("SIGUIENTE HOYO")
                        builder.setMessage("??Continuar al siguiente hoyo?")
                        /*Cuando confirme, registraremos el resultado del hoyo y pasaremos
                        * al siguiente*/
                        builder.setPositiveButton("ACEPTAR",DialogInterface.OnClickListener{
                            dialog,which->
                            db.collection("usuarios").document(usuario)
                                .collection("campos").document(campoSeleccionado).get()
                                .addOnSuccessListener {
                                    if (hoyo <=18) {
                                        binding.handicapHoyo.setText(it.get("hcap " + hoyo)
                                                as String?) //Cambiamos el handicap del hoyo
                                        binding.parHoyo.setText(it.get("par " + hoyo)
                                                as String?) //Cambiamos el par del hoyo
                                        binding.numHoyo.setText(hoyo.toString())
                                        //Cambiamos el hoyo
                                    }
                                }
                                partido.setGolpe(
                                    binding.golpes.text.toString(),
                                    hoyo.toString(),
                                    idPartido.toString()
                                ) //Guardamos el n??mero de golpes

                            var par = binding.parHoyo.text.toString().toInt()
                            var golpes = binding.golpes.text.toString().toInt()
                            var hcpHoyo = binding.handicapHoyo.text.toString().toInt()
                            partido.puntos(par, golpes,hcpHoyo, handicap,puntos)
                            puntos = partido.getPuntos()
                            /*Si estamos en el ??ltimo hoyo pasaremos al resumen del partido*/
                            if(hoyo == 18){
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

        /*Al pulsar bot??n cancelar
    * volveremos a la activity SeleccionTarjeta y eliminaremos los datos del partido
    * que est??bamos jugando, para no tener registros incompletos en la base de datos.*/
        binding.botonCancelar.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("SALIR")
            builder.setMessage("??Seguro que desea salir del partido?")
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

    /*--------------------------------------Funciones---------------------------------------------*/

    /*Nos permite guardar algunos registros para que, si el usuario cambia de aplicaci??n,
    * cuando vuelva a esta, si se diese el caso de que la activity se ha reiniciado, pueda
    * recuperar la informaci??n de la partida que estaba jugando*/
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("hoyo",hoyo)
    }
    /*Nos permite guardar algunos registros para que, si el usuario cambia de aplicaci??n,
    * cuando vuelva a esta, si se diese el caso de que la activity se ha reiniciado, pueda
    * recuperar la informaci??n de la partida que estaba jugando*/
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        hoyo = savedInstanceState.getInt("hoyo")
    }

    /*Al pulsar dos veces seguidas en menos de dos segundos el bot??n atr??s
    * volveremos a la activity de selecci??n de pantalla y eliminaremos los datos del partido
    * que est??bamos jugando, para no tener registros incompletos en la base de datos.*/
    override fun onBackPressed() {
        val idPressedBack = intent.getStringExtra("idPartido")
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed()
            if (idPressedBack != null) {
                db.collection("usuarios").document(usuario)
                    .collection("partidos").document(idPressedBack).delete()
            }
        }else{
            Toast.makeText(this,"Pulsa atr??s otra vez para salir", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

}