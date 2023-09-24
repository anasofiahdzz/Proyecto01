package com.sodiri.tamarweather.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.sodiri.tamarweather.dataclimas.ClimaApi
import com.sodiri.tamarweather.dataclimas.ClimaResponse
import com.sodiri.tamarweather.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val actualizaClima: Button = findViewById(R.id.actualizaClima)

        //Actualiza el clima una vez es presionado
        actualizaClima.setOnClickListener {
            Toast.makeText(this, "Actualizando...", Toast.LENGTH_LONG).show()
        }

        val consultarTicket: Button = findViewById(R.id.gticket)

        //Inicia la pantalla de consulta de ticket en caso de que sea presionado
        consultarTicket.setOnClickListener {
            val intento : Intent = Intent(this, TicketActivity::class.java)
            startActivity(intento)
        }

        val ciudad = "London,uk"
        val llaveApi = "f6adade6884dd6823a33809867ec6cb9"

        //Creación del retrofit a partir de la url base
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val servicio = retrofit.create(ClimaApi::class.java)

        val llamada = servicio.obtenerClima(ciudad, llaveApi)

        //Llamada a la API con la asignación de datos obtenidos
        llamada.enqueue(object : Callback<List<ClimaResponse>> {
            override fun onResponse(
                call: Call<List<ClimaResponse>>,
                response: Response<List<ClimaResponse>>
            ) {
                if (response.isSuccessful) {
                    val datos = response.body()
                    if (datos != null){
                        var ciudad = datos[6].name
                        var grados = datos[5].main[3].temp
                        var latitud = datos[2].coord[0].lat
                        var longitud = datos[0].coord[1].lon
                    }
                }
            }
            override fun onFailure(call: Call<List<ClimaResponse>>, t: Throwable) {
                    println("Hubo fallo en ${t.message}")
            }
        })
    }
}

