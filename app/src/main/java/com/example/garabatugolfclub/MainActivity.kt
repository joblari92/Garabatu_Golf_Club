package com.example.garabatugolfclub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(2000)
        /**
         * Dejamos un tiempo para que se muestre
         * la Splashscreen antes de que se inicie la aplicación
         * y después cambiamos al tema principal
         * */
        setTheme(R.style.Theme_GarabatuGolfClub)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}