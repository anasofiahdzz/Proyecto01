package com.sodiri.tamarweather.activities

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.sodiri.tamarweather.R
import org.json.JSONObject
import java.net.URL


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actualizaClima: Button = findViewById(R.id.actualizaClima)
        actualizaClima.setOnClickListener {
            Toast.makeText(this, "Actualizando...", Toast.LENGTH_LONG).show()
            obtenClima().execute()
        }

        val consultarTicket: Button = findViewById(R.id.gTicket)
        consultarTicket.setOnClickListener {
            val intento = Intent(this, TicketActivity::class.java)
            startActivity(intento)
        }
    }

    val llave : String = "f6adade6884dd6823a33809867ec6cb9"

    /**
     * Clase interna asíncrona para llamar a la Api de OpenWeather
     * Propociona los datos requeridos dada la ciudad introducida por
     * el usuario
     */
    inner class obtenClima : AsyncTask<String, Void, String>(){

        /**
         * Método que realiza la solicitud de la API en segundo plano.
         * @param p0 Arreglo de cadenas que contiene la ciudad proporcionada.
         * @return Respuesta de la solicitud como una cadena JSON.
         */
        override fun doInBackground(vararg p0: String?): String? {

            var respuesta: String?
            try {
                val ciudadIntro = findViewById<EditText>(R.id.textCiudadInt)
                val ciudad = ciudadIntro.text.toString()

                respuesta = URL("https://api.openweathermap.org/data/2.5/weather?q=$ciudad&units=metric&appid=$llave&lang=sp")
                    .readText(Charsets.UTF_8)
            }catch (e: Exception){
                respuesta = null
            }
            return respuesta
        }

        /**
         * Método llamado después de que se completa la solicitud en segundo plano.
         *
         * @param result Resultado de la solicitud en segundo plano como una cadena JSON.
         */
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try{
                val objetoJSON = JSONObject(result)
                val main = objetoJSON.getJSONObject("main")
                val clima = objetoJSON.getJSONArray("weather").getJSONObject(0)
                val coordenadas = objetoJSON.getJSONObject("coord")

                val ciudadActual = objetoJSON.getString("name")
                val descripción = clima.getString("description")
                val temperaturaActual = main.getString("temp") + "°C"
                val temperaturaMax = "Temperatura máxima: \n" + main.getString("temp_max") + "°C"
                val temperaturaMin = "Temperatura mínima: \n" + main.getString("temp_min") + "°C"
                val latitud = "Latitud: \n"+ coordenadas.getString("lat")
                val longitud = "Longitud: \n"+ coordenadas.getString("lon")

                findViewById<TextView>(R.id.textCiudadPrin).text = ciudadActual
                findViewById<TextView>(R.id.textDescripcion).text = descripción
                findViewById<TextView>(R.id.textLatitud).text = latitud
                findViewById<TextView>(R.id.textLongitud).text = longitud
                findViewById<TextView>(R.id.textGrados).text = temperaturaActual
                findViewById<TextView>(R.id.textTempMax).text = temperaturaMax
                findViewById<TextView>(R.id.textTempMin).text = temperaturaMin

            }catch (e: Exception){
                findViewById<TextView>(R.id.textCiudadPrin).text = null
                findViewById<TextView>(R.id.textDescripcion).text = "Fallo al consultar clima"
                findViewById<TextView>(R.id.textLatitud).text = null
                findViewById<TextView>(R.id.textLongitud).text = null
                findViewById<TextView>(R.id.textGrados).text = null
                findViewById<TextView>(R.id.textTempMax).text = null
                findViewById<TextView>(R.id.textTempMin).text = null
            }
        }
    }
}

