package com.example.garabatugolfclub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.garabatugolfclub.databinding.ActivityMainBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val GOOGLE_SIGN_IN = 100
    private val callbackManager = CallbackManager.Factory.create()

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

        /*Acción al pulsar el botón de FACEBOOK
        * IMPORTANTE: al estar la aplicación en desarrollo y así reflejarse en la web de
        * desarrollo de Facebook, solo se puede acceder mediante los usuarios autorizados.
        * En este caso el usuario es open_whyrssc_user@tfbnw.net y la contraseña Garabatugolfclub2021*/
        binding.facebookButton.setOnClickListener {

            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))

            LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {

                override fun onSuccess(result: LoginResult?) {

                    result?.let {

                        val token = it.accessToken

                        val credential = FacebookAuthProvider.getCredential(token.token)
                        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Log.d("signIn success", "signInWithEmail:success")
                                val user = auth.currentUser
                                Toast.makeText(baseContext, "Sesión iniciada",
                                    Toast.LENGTH_SHORT).show()
                                LaunchActivityInicio()
                            } else {
                                Log.w("signIn fail", "signInWithEmail:failure", it.exception)
                                Toast.makeText(baseContext, "Inicio de sesión fallido",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {
                    Toast.makeText(baseContext, "Inicio de sesión fallido",
                        Toast.LENGTH_SHORT).show()
                }
            })


        }




    }

    override fun onDestroy() {

        super.onDestroy()
    }



    /*----------------------------------------FUNCIONES------------------------------------------*/


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

    private fun LaunchActivityInicio(){
        val i = Intent(this,Inicio::class.java)
        startActivity(i)
    }

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

        callbackManager.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if(account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
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
            } catch (e: ApiException) {
                Log.w("signIn fail", "Google sign in failed", e)
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