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
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val GOOGLE_SIGN_IN = 100

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

        /*Inicializamos Firebase Auth*/
        auth = Firebase.auth

        /*Acción al pulsar el botón REGISTRO*/
        binding.registro.setOnClickListener {
            if (binding.email.text.contains("@") && binding.password.text.isNotEmpty()){
                createAccount(binding.email.text.toString(),binding.password.text.toString())
            }else{
                Toast.makeText(baseContext,"Datos de registro no válidos",Toast.LENGTH_SHORT).show()
            }

        }

        /*Acción al pulsar el botón INICIO*/
        binding.inicio.setOnClickListener {
            if (binding.email.text.contains("@") && binding.password.text.isNotEmpty()){
                signIn(binding.email.text.toString(),binding.password.text.toString())
            }else{
                Toast.makeText(baseContext,"Datos de inicio no válidos",Toast.LENGTH_SHORT).show()
            }
        }

        /*Acción al pulsar el botón de GOOGLE*/
        binding.googleButton.setOnClickListener {
            //Configuración
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this,googleConf)
            googleClient.signOut() //Cerrará la sesión actual para poder autenticarnos con otra
                                    // en caso de tener varias cuentas de Google en el dispositivo
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        }



    }

    override fun onDestroy() {

        super.onDestroy()
    }


    /*private fun reload() {
        val i = Intent(this, Inicio::class.java)
                    startActivity(i)
    }*/

    /*Comprobamos si el usuario ya ha iniciado sesión, de ser así
         *cargamos la siguiente activity con la función reload*/
    /*public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload();
        }
    }*/

    /*Función de registro de nuevos usuarios*/
    private fun createAccount(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("logIn success", "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "Usuario registrado",
                        Toast.LENGTH_SHORT).show()
                    val i = Intent(this, Inicio::class.java)
                    startActivity(i)
                    //reload()
                } else {
                    Log.w("logIn fail", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Registro fallido",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    /*Función de inicio de sesión*/
    private fun signIn(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("signIn success", "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "Sesión iniciada",
                        Toast.LENGTH_SHORT).show()
                    val i = Intent(this, Inicio::class.java)
                    startActivity(i)
                } else {
                    Log.w("signIn fail", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Inicio de sesión fallido",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    /*Lo utilizaremos para iniciar sesión con Google*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d("signIn success", "firebaseAuthWithGoogle:" + account.email)
                firebaseAuthWithGoogle(account.idToken!!)
                val user = auth.currentUser

                val i = Intent(this, Inicio::class.java)
                startActivity(i)
                if (user != null) {
                    Toast.makeText(baseContext, "Sesión iniciada" + " " + user.email,
                        Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Log.w("signIn fail", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("signIn success", "signInWithCredential:success")
                } else {
                    Log.w("signIn fail", "signInWithCredential:failure", task.exception)
                }
            }
    }


    /*Utilizamos esta función para obtener el hash que nos
     *permita iniciar sesión con Facebook, pudiendo encontrarlo en el Logcat

    fun generateFBKey () {
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