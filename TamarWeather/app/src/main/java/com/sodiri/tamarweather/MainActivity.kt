package com.sodiri.tamarweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private var iataociudad: EditText? = null
    private var temperaturaActual: TextView? = null
    //private var service: WeatherService? = null

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

    vistasInterfaz()
    weather = Weather("5da48e480d2042d2d7f572f164a77d2b")

    private fun vistasInterfaz() {
        iataociudad = findViewById(R.id.iataociudad)
        temperaturaActual = findViewById(R.id.temperaturaActual)
    }

    fun obtenerInfoMetereologica(view: View) {
        val alerta = AlertDialog.Builder(this)
        val texto = StringBuilder()

        val ciudadIATACode = iataociudad?.text.toString()

        if (ciudadIATACode.isEmpty()) {
            texto.append(getString(R.string.Fields_cannot_be_empty))
            alerta.setMessage(texto)
            alerta.setPositiveButton("close", null)
            alerta.show()
        } else {
            getInfoClima(ciudadIATACode)
        }
    }

    private fun getInfoClima(ciudadIATACode: String) {
        service?.requestWeatherDataByIATACode(ciudadIATACode) { isNetworkError, statusCode, root ->
            if (!isNetworkError) {
                if (statusCode == 200) {
                    mostrarInfoCLima(root)
                } else {
                    Log.d("Clima", "Service error")
                }
            } else {
                Log.d("Clima", "Error de Red")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun mostrarInfoCLima(root: Root) {
        val temp = root.main?.temp.toString()

        temperaturaActual?.text = getString(R.string.actual) + " " + temp
    }
}