package com.example.garabatugolfclub

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.garabatugolfclub.databinding.ActivityTarjetaAvanzadaBinding
import com.google.android.gms.location.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import io.grpc.InternalChannelz.id

class TarjetaAvanzada : AppCompatActivity() {
    private lateinit var binding: ActivityTarjetaAvanzadaBinding

    //VARIABLES GPS
    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var latitud = 0.0
    var longitud = 0.0
    val startPoint = Location("locationA")
    val endPoint = Location("locationA")

    //Variable para evitar salir de la activity directamente
    private var backPressedTime = 0L

    //Variables
    val usuario = Firebase.auth.currentUser?.email.toString()
    val db = FirebaseFirestore.getInstance()
    val partido = Partidos(usuario)
    var hoyo = 1
    var puntos = 0
    var totalGolpes = 0
    val golpe = Golpes(usuario)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GarabatuGolfClub)

        super.onCreate(savedInstanceState)
        binding = ActivityTarjetaAvanzadaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val campoSeleccionado = intent.getStringExtra("campoSeleccionado")//Recuperamos nombre del campo
        // de la activity anterior
        val idPartido = intent.getStringExtra("idPartido") //Recuperamos el ID del partido que se está
        //jugando para almacenar los resultados

        //GPS
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


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

        //Desplegable en el que seleccionaremos el palo que vamos a utilizar
        val palos = resources.getStringArray(R.array.palos)
        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item,palos)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        //Pasamos de hoyo
        binding.botonSiguiente.setOnClickListener {
            if(binding.golpes.text.isNotEmpty()) {
                if (hoyo <= 18) {
                    if (idPartido != null && campoSeleccionado != null) {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("SIGUIENTE HOYO")
                        builder.setMessage("¿Continuar al siguiente hoyo?")
                        builder.setPositiveButton("ACEPTAR", DialogInterface.OnClickListener{
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
                                totalGolpes = 0
                                binding.distancia.setText(null)
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

        //Lanzamos la función gps una primera vez para que el usuario acepte o rechace los permisos
        getLastLocation()
        Log.d("gps","Latitud: " + latitud + " Longitud: " + longitud)

        //lugar del GOLPE
        binding.botonGolpe.setOnClickListener {
            val dropdown = binding.autoCompleteTextView.text
            if (dropdown.isNotEmpty()) {//Primero deberemos seleccionar un palo del listado
                getLastLocation()
                startPoint.setLatitude(latitud)
                startPoint.setLongitude(longitud)
                Log.d("gps", "Latitud: " + latitud + " Longitud: " + longitud)
                binding.botonGolpe.isEnabled = false
            }else{
                Toast.makeText(baseContext, "Seleccione un palo", Toast.LENGTH_SHORT).show()
            }
        }

        //DESTINO del golpe
        binding.botonDestino.setOnClickListener {
            val palo = binding.autoCompleteTextView.text
            if(binding.botonGolpe.isEnabled == true){//Primero deberá registrarse el punto de golpeo de la bola
                Toast.makeText(baseContext, "Registre primero el golpe", Toast.LENGTH_SHORT).show()
            }else {
                getLastLocation()
                endPoint.setLatitude(latitud)
                endPoint.setLongitude(longitud)
                Log.d("gps", "Latitud: " + latitud + " Longitud: " + longitud)
                val distance: Float = startPoint.distanceTo(endPoint)
                binding.distancia.setText(Math.round(distance).toString() + " metros")
                binding.botonGolpe.isEnabled = true
                binding.autoCompleteTextView.setText(null)
                totalGolpes++
                binding.golpes.setText(totalGolpes.toString())
                golpe.setGolpe(campoSeleccionado.toString(),palo.toString(),Math.round(distance).toString())

            }
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

    /*--------------------------------------FUNCIONES GPS-----------------------------------------*/

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        requestNewLocationData()
                        latitud = location.latitude
                        longitud = location.longitude
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d("gps","coordinadas registradas")
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

}