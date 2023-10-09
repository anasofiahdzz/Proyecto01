package com.sodiri.tamarweather.activities

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.sodiri.tamarweather.R
import datos.CodigosIATA
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.json.JSONObject
import java.io.BufferedReader
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date


open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actualizaClima : Button = findViewById(R.id.actualizaClima)
        //Actualiza el clima una vez la ciudad ha sido introducida
        actualizaClima.setOnClickListener {
            Toast.makeText(this, "Actualizando...", Toast.LENGTH_LONG).show()
            obtenFecha()
            obtenCiudadUI()
            llamadoIATACiudad()
        }

        val consultarTicket: Button = findViewById(R.id.gTicket)
        //Cambia a la actividad donde se introducen los tickets del usuario a consultar.
        consultarTicket.setOnClickListener {
            val intento = Intent(this, TicketActivity::class.java)
            startActivity(intento)
        }
    }

    lateinit var listaCodigos : List<CodigosIATA>
    lateinit var ciudadOIATA : String
    lateinit var latitudIATA : String
    lateinit var longitudIATA: String

    val llave : String = "f6adade6884dd6823a33809867ec6cb9"

    private fun obtenFecha(){
        val fechaAct = Date()
        val formato = SimpleDateFormat("dd-MM-yyyy")
        val fechaConFormato = formato.format(fechaAct)

        findViewById<TextView>(R.id.textFecha).text = fechaConFormato
    }

    /**
     * Metodo que selecciona si la entrada recibida por el usuario
     * es una IATA o una ciudad y elige la corrutina a ejecutar.
     */
    private fun llamadoIATACiudad(){
      if(ciudadOIATA.length <= 4){
        coordenadasIATA()
          obtenClimaIATA().execute()
        }else{
          obtenClimaCiudad().execute()
        }
    }

    /**
     * Metodo que obtiene la ciudad, IATA o String introducido por el usuario
     * y la asigna a ciudad o IATA
     */
    private fun obtenCiudadUI(){
        ciudadOIATA = findViewById<AutoCompleteTextView>(R.id.textCiudadInt).text.toString()
        if(ciudadOIATA.length <= 1){
            Toast.makeText(this, "Introduzca algo no vacío", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Método que lee un csv y crea una lista con sus respectivos datos de la
     * data class CodigosIATA
     * @return regresa una lista con todos los datos de la IATA a buscar
     */
    private fun listaIATA(): List<CodigosIATA>{
        val buffer = BufferedReader(assets.open("airport_codes.csv").reader())
        val analizadorCSV = CSVParser.parse(buffer, CSVFormat.DEFAULT)
        val listaIATAS = mutableListOf<CodigosIATA>()

        analizadorCSV.forEach {
            it.let {
                val codigosIATA = CodigosIATA(
                    ident = it.get(0),
                    type = it.get(1),
                    name = it.get(2),
                    elevation_ft = it.get(3),
                    continent = it.get(4),
                    iso_country = it.get(5),
                    iso_region = it.get(6),
                    municipality = it.get(7),
                    gps_code = it.get(8),
                    iata_code = it.get(9),
                    local_code = it.get(10),
                    coordinates = it.get(11),
                )
                listaIATAS.add(codigosIATA)
            }
        }
        return listaIATAS
    }

    /**
     * Método que obtiene los datos específicos de la iata busca dentro
     * de la lista de IATAs
     * @return lista con los datos de la IATA especificada
     */
    private fun obtenDatosIATA(codigoIATA: String): CodigosIATA? {
        listaCodigos = listaIATA()
        return listaCodigos.find { it.iata_code == codigoIATA }
    }


    /**
     * Método que asigna las coordenadas a sus respectivos valores
     * de latitud y longitud para ser ocupadas después por la API
     */
    private fun coordenadasIATA(){
        val iataBuscada = obtenDatosIATA(ciudadOIATA)
        if(iataBuscada != null){
            val coordenadas = iataBuscada.coordinates
            val separador = coordenadas.split(",")
            latitudIATA = separador[1].trim()
            longitudIATA = separador[0].trim()
        }
    }

    /**
     * Clase interna asíncrona para llamar a la Api de OpenWeather
     * Propociona los datos requeridos dada la ciudad introducida por
     * el usuario
     */
    inner class obtenClimaCiudad : AsyncTask<String, Void, String>(){

        /**
         * Método que realiza la solicitud de la API en segundo plano.
         * @param p0 Arreglo de cadenas que contiene la ciudad proporcionada.
         * @return Respuesta de la solicitud como una cadena JSON.
         */
        override fun doInBackground(vararg p0: String?): String? {

            var respuesta: String?
            try {
                respuesta = URL("https://api.openweathermap.org/data/2.5/weather?q=$ciudadOIATA&units=metric&appid=$llave&lang=sp")
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
                val temperaturaMax = "Temp. máxima: \n" + main.getString("temp_max") + "°C"
                val temperaturaMin = "Temp. mínima: \n" + main.getString("temp_min") + "°C"
                val latitud = "Latitud: \n"+ coordenadas.getString("lat")
                val longitud = "Longitud: \n"+ coordenadas.getString("lon")
                val humedad = "Humedad: \n" + main.getString("humidity") + "%"
                val sensacionTerm = "Sensación: \n" + main.getString("feels_like") + "°C"


                findViewById<TextView>(R.id.textCiudad).text = ciudadActual
                findViewById<TextView>(R.id.textDescripcion).text = descripción
                findViewById<TextView>(R.id.textLatitud).text = latitud
                findViewById<TextView>(R.id.textLongitud).text = longitud
                findViewById<TextView>(R.id.textGrados).text = temperaturaActual
                findViewById<TextView>(R.id.textTempMax).text = temperaturaMax
                findViewById<TextView>(R.id.textTempMin).text = temperaturaMin
                findViewById<TextView>(R.id.textSensTerm).text = sensacionTerm
                findViewById<TextView>(R.id.textHumedad).text = humedad

            }catch (e: Exception){
                findViewById<TextView>(R.id.textCiudad).text = null
                findViewById<TextView>(R.id.textDescripcion).text = "Fallo al consultar clima"
                findViewById<TextView>(R.id.textLatitud).text = null
                findViewById<TextView>(R.id.textLongitud).text = null
                findViewById<TextView>(R.id.textGrados).text = null
                findViewById<TextView>(R.id.textTempMax).text = null
                findViewById<TextView>(R.id.textTempMin).text = null
            }
        }
    }





    /**
     * Clase interna asíncrona para llamar a la Api de OpenWeather
     * Propociona los datos requeridos dada la ciudad introducida por
     * el usuario
     */
    inner class obtenClimaIATA : AsyncTask<String, Void, String>(){

        /**
         * Método que realiza la solicitud de la API en segundo plano.
         * @param p0 Arreglo de cadenas que contiene la ciudad proporcionada.
         * @return Respuesta de la solicitud como una cadena JSON.
         */
        override fun doInBackground(vararg p0: String?): String? {

            var respuestaIATA: String?
            try {
                respuestaIATA = URL("https://api.openweathermap.org/data/2.5/weather?lat=$latitudIATA&lon=$longitudIATA&appid=$llave&units=metric&lang=es")
                    .readText(Charsets.UTF_8)
            }catch (e: Exception){
                respuestaIATA = null
            }
            return respuestaIATA
        }

        /**
         * Método llamado después de que se completa la solicitud en segundo plano.
         *
         * @param result Resultado de la solicitud en segundo plano como una cadena JSON.
         */
        override fun onPostExecute(resultado: String?) {

            super.onPostExecute(resultado)
            try{
                val objetoJSON = JSONObject(resultado)
                val main = objetoJSON.getJSONObject("main")
                val clima = objetoJSON.getJSONArray("weather").getJSONObject(0)
                val coordenadas = objetoJSON.getJSONObject("coord")

                val ciudadActual = objetoJSON.getString("name")
                val descripción = clima.getString("description")
                val temperaturaActual = main.getString("temp") + "°C"
                val temperaturaMax = "Temp. máxima: \n" + main.getString("temp_max") + "°C"
                val temperaturaMin = "Temp. mínima: \n" + main.getString("temp_min") + "°C"
                val latitud = "Latitud: \n"+ coordenadas.getString("lat")
                val longitud = "Longitud: \n"+ coordenadas.getString("lon")
                val humedad = "Humedad: \n" + main.getString("humidity") + "%"
                val sensacionTerm = "Sensación: \n" + main.getString("feels_like") + "°C"

                findViewById<TextView>(R.id.textCiudad).text = ciudadActual
                findViewById<TextView>(R.id.textDescripcion).text = descripción
                findViewById<TextView>(R.id.textLatitud).text = latitud
                findViewById<TextView>(R.id.textLongitud).text = longitud
                findViewById<TextView>(R.id.textGrados).text = temperaturaActual
                findViewById<TextView>(R.id.textTempMax).text = temperaturaMax
                findViewById<TextView>(R.id.textTempMin).text = temperaturaMin
                findViewById<TextView>(R.id.textSensTerm).text = sensacionTerm
                findViewById<TextView>(R.id.textHumedad).text = humedad

            }catch (e: Exception){
                findViewById<TextView>(R.id.textCiudad).text = null
                findViewById<TextView>(R.id.textDescripcion).text = "Fallo al consultar clima"
                findViewById<TextView>(R.id.textLatitud).text = null
                findViewById<TextView>(R.id.textLongitud).text = null
                findViewById<TextView>(R.id.textGrados).text = null
                findViewById<TextView>(R.id.textTempMax).text = null
                findViewById<TextView>(R.id.textTempMin).text = null
                findViewById<TextView>(R.id.textSensTerm).text = null
                findViewById<TextView>(R.id.textHumedad).text = null
            }
        }
    }
}

