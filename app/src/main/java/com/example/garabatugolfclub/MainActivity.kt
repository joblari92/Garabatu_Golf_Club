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
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
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
        /*
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
        /*
         * Utilizaremos el método viewBinding tanto en esta activity como
         * en todas las que vayamos a crear.
         */

        // Inicializamos Firebase Auth
        auth = Firebase.auth

        binding.registro.setOnClickListener {
            if (binding.email.text.contains("@") && binding.password.text.isNotEmpty()){
                createAccount(binding.email.text.toString(),binding.password.text.toString())
            }else{
                Toast.makeText(baseContext,"Datos de registro no válidos",Toast.LENGTH_SHORT).show()
            }

        }


        //generateFBKey() //Utilizamos esta función para obtener el hash que nos
                        //permita iniciar sesión con Facebook
    }


    private fun reload() {
        TODO()
    }

    /*Comprobamos si el usuario ya ha iniciado sesión, de ser así
         *cargamos la siguiente activity con la función reload*/
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }

    /*Función de registro de nuevos usuarios*/
    private fun createAccount(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email,
        password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("reg.exi.", "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "Usuario registrado",
                        Toast.LENGTH_SHORT).show()
                    reload()
                } else {
                    Log.w("fail.reg", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }

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

}