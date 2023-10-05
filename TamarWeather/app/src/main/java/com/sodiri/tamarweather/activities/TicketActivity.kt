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
import datas.Ticket
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.json.JSONObject
import java.io.BufferedReader
import java.net.URL


class TicketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)

        val buscarTicket: Button = findViewById(R.id.buscaTicket)
        buscarTicket.setOnClickListener{
            Toast.makeText(this, "Buscando...", Toast.LENGTH_LONG).show()
            imprimeTicket()
            asignaCoordenadas()
            obtenClimaORTicket().execute()
            obtenClimaDSTicket().execute()
        }

        val pantallaInicial : Button = findViewById(R.id.regreso)
        pantallaInicial.setOnClickListener {
            val regreso = Intent(this, MainActivity::class.java)
            startActivity(regreso)
        }
    }

    val llave : String = "f6adade6884dd6823a33809867ec6cb9"
    lateinit var listaTickets : List<Ticket>
    lateinit var ticket: String
    lateinit var latitudOR : String
    lateinit var longitudOR : String
    lateinit var latitudDES : String
    lateinit var longitudDES : String

    /**
     * Función que extrae los datos introducidos en la pantalla
     * y los asigna a la variable ticket
     */
    private fun imprimeTicket(){
        ticket = findViewById<EditText>(R.id.textRecibidorTicket).text.toString()
    }

    /**
     * Lee el archivo CSV y regresa una lista de los tickets obtenidos
     * con sus respectivos datos como
     * el numero de ticket, origen destino, latitudes y longitudes.
     * @return lista con todos los tickets dentro del CSV
     */
    private fun leeCSV() : List<Ticket>{
        val buffer = BufferedReader(assets.open("dataset2.csv").reader())
        val csvParser = CSVParser.parse(buffer, CSVFormat.DEFAULT)
        val listaTicket = mutableListOf<Ticket>()
        csvParser.forEach {
            it?.let {
                val tickets = Ticket(
                    num_ticket = it.get(0),
                    origin = it.get(1),
                    destination = it.get(2),
                    origin_latitude = it.get(3),
                    origin_longitude = it.get(4),
                    destination_latitude = it.get(5),
                    destination_longitude = it.get(6),
                )
                listaTicket.add(tickets)
            }
        }
        return listaTicket
    }

    /**
     * Función que va a recibir un ticket el cual va a buscar y regresar
     * si el ticket fue encontrado
     * @param ticket ticket que vamos a buscar para encontrar sus respectivos datos
     * @return ticket hallado con sus respectivos datos
     */
    private fun obtenenDatosTicket(ticket: String) : Ticket? {
        listaTickets = leeCSV()
        val ticketEncontrado = listaTickets.find { it.num_ticket == ticket }
        return ticketEncontrado
    }

    /**
     * Función que asigna las respectivas coordenas en las variables
     * de latitudes y longitudes en base al ticket hallado en caso
     * de que el mismo no sea nulo.
     */
    private fun asignaCoordenadas(){
        val ticketBuscado = obtenenDatosTicket(ticket)
        if(ticketBuscado != null){
            latitudOR = ticketBuscado.origin_latitude
            longitudOR = ticketBuscado.origin_longitude
            latitudDES = ticketBuscado.destination_latitude
            longitudDES = ticketBuscado.origin_longitude
        }else{
            println("Error, ticket no hallado.")
        }
    }



    /**
     * Clase interna asíncrona para llamar a la Api de OpenWeather
     * en función del ticket.
     * Proporciona los datos climáticos de la ciudad de origen del viajero.
     */
    inner class obtenClimaORTicket : AsyncTask<String, Void, String>() {

        /**
         * Método que realiza la solicitud en segundo plano a la API
         * de OpenWeather en base a las coordenadas de origen del ticket
         * @param p0 Arreglo de cadenas que contiene la ciudad proporcionada.
         * @return Respuesta de la solicitud como una cadena JSON.
         */
        override fun doInBackground(vararg p0: String?): String? {
            var respuesta: String?
            try {
                respuesta =
                    URL("https://api.openweathermap.org/data/2.5/weather?lat=$latitudOR&lon=$longitudOR&appid=$llave&units=metric&lang=es")
                        .readText(Charsets.UTF_8)
            } catch (e: Exception) {
                respuesta = null
            }
            return respuesta
        }

        /**
         * Método llamado después de que se completa la solicitud en segundo plano.
         *
         * @param result Resultado de la solicitud en segundo plano como una cadena JSON.
         */
        override fun onPostExecute(resultado: String?) {
            super.onPostExecute(resultado)
            try {
                val objetoJSON = JSONObject(resultado)
                val main = objetoJSON.getJSONObject("main")
                val coordenadas = objetoJSON.getJSONObject("coord")

                val ciudad1 = objetoJSON.getString("name")
                val temperaturaActual1 = main.getString("temp") + "°C"
                val latitud1 = "Latitud: " + coordenadas.getString("lat")
                val longitud1 = "Longitud: " + coordenadas.getString("lon")

                findViewById<TextView>(R.id.textCiudadIda1).text = ciudad1
                findViewById<TextView>(R.id.textLatCiudad1).text = latitud1
                findViewById<TextView>(R.id.textLongCiudad1).text = longitud1
                findViewById<TextView>(R.id.textGradosCiudad1).text = temperaturaActual1
            } catch (e: Exception) {
                findViewById<TextView>(R.id.textGradosCiudad1).text = "error"
                findViewById<TextView>(R.id.textLatCiudad1).text = "error"
                findViewById<TextView>(R.id.textLongCiudad1).text = "error"
                findViewById<TextView>(R.id.textCiudadIda1).text = "No encontrado"
            }
        }
    }





    /**
     * Clase interna asíncrona para llamar a la Api de OpenWeather
     * en función del ticket.
     * Proporciona los datos de la ciudad de destino del viajero
     */
    inner class obtenClimaDSTicket : AsyncTask<String, Void, String>() {

        /**
         * Método que relaliza la solicitud a la API de OpenWeather
         * en base a las coordenadas de destino del ticket
         * @param p0 Arreglo de cadenas que contiene la ciudad proporcionada.
         * @return Respuesta de la solicitud como una cadena JSON.
         */
        override fun doInBackground(vararg p0: String?): String? {

            var respuesta: String?
            try {
                respuesta =
                    URL("https://api.openweathermap.org/data/2.5/weather?lat=$latitudDES&lon=$longitudDES&appid=$llave&units=metric&lang=es")
                        .readText(Charsets.UTF_8)
            } catch (e: Exception) {
                respuesta = null
            }
            return respuesta
        }

        /**
         * Método llamado después de que se completa la solicitud en segundo plano.
         *
         * @param result Resultado de la solicitud en segundo plano como una cadena JSON.
         */
        override fun onPostExecute(resultado: String?) {
            super.onPostExecute(resultado)
            try {
                val objetoJSON = JSONObject(resultado)
                val main = objetoJSON.getJSONObject("main")
                val coordenadas = objetoJSON.getJSONObject("coord")

                val ciudad2 = objetoJSON.getString("name")
                val temperaturaActual2 = main.getString("temp") + "°C"
                val latitud2 = "Latitud: " + coordenadas.getString("lat")
                val longitud2 = "Longitud: " + coordenadas.getString("lon")

                findViewById<TextView>(R.id.textCiudadIda2).text = ciudad2
                findViewById<TextView>(R.id.textLatCiudad2).text = latitud2
                findViewById<TextView>(R.id.textLongCiudad2).text = longitud2
                findViewById<TextView>(R.id.textGradosCiudad2).text = temperaturaActual2
            } catch (e: Exception) {
                findViewById<TextView>(R.id.textGradosCiudad2).text = "error"
                findViewById<TextView>(R.id.textLatCiudad2).text = "error"
                findViewById<TextView>(R.id.textLongCiudad2).text = "error"
                findViewById<TextView>(R.id.textCiudadIda2).text = "No encontrado"
            }
        }
    }
}