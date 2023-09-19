package com.sodiri.tamarweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Encuentra al botón del actualización por medio de su id de botón
        val actualizaClima: Button = findViewById(R.id.actualizaClima)

        //función que se activa una vez que es presionado
        actualizaClima.setOnClickListener {
            //Aqui añadir una función de congelamiento
            Toast.makeText(this, "Actualizando...", Toast.LENGTH_LONG).show()
        }
    }
}