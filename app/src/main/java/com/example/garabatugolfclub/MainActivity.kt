package com.example.garabatugolfclub

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.garabatugolfclub.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(2000)
        /**
         * Dejamos un tiempo, aunque puede que
         * no sea necesario,para que se muestre
         * la Splashscreen antes de que se inicie
         * la aplicación y después cambiamos
         * al tema principal
         * */
        setTheme(R.style.Theme_GarabatuGolfClub)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        /**
         * Utilizaremos el método viewBinding tanto en esta activity como
         * en todas las que vayamos a crear.
         */

        //Setup
        //setup()

        //generateFBKey() //Utilizamos esta función para obtener el hash que nos
                        //permita iniciar sesión con Facebook
    }

    /*fun generateFBKey () {
        try {
            val info = packageManager.getPackageInfo (
                "com.example.garabatugolfclub",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance ("SHA")
                md.update (signature.toByteArray ())
                Log.d ("KeyHash:", Base64.encodeToString (md.digest (), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }*/

    /*private fun setup() {

        title = "Autenticación"

        binding.registro.setOnClickListener {

            if (binding.email.text.isNotEmpty() && binding.password.text.isNotEmpty()) {

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(binding.email.text.toString(),
                        binding.password.text.toString()).addOnCompleteListener {
                            if (it.isSuccessful) {
                                showInicio(it.result?.user?.email ?:"")
                            } else {
                                showAlert()
                            }
                    }
            } else {

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("Primer if")
                builder.setPositiveButton("Aceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()

            }
        }
    }*/

    /*Función para crear mensaje de alerta en caso de
      no poder iniciar sesión */
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /*Función para lanzar la siguiente actividad*/
   /* private fun showInicio(email: String) {
        val inicioIntent = Intent(this, Inicio::class.java).apply {
            putExtra("email", email)
        }
        startActivity(inicioIntent)
    }*/
}