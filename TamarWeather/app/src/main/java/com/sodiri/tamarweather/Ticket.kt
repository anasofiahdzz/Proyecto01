package com.sodiri.tamarweather
import com.opencsv.CSVReader
import java.io.FileReader
import java.util.ArrayList

class Ticket(
    ticketNumber: Int,
    originCityIataCode: String,
    destinationCityIataCode: String,
    originLatitude: Double,
    originLongitude: Double,
    destinationLatitude: Double,
    destinationLongitude: Double
) {
    val ticketNumber: Int,
    val originCityIataCode: String,
    val destinationCityIataCode: String,
    val originLatitude: Double,
    val originLongitude: Double,
    val destinationLatitude: Double,
    val destinationLongitude: Double,
}

fun readTicketsFromCSV(csvFilePath: String): List<Ticket> {
    val tickets = ArrayList<Ticket>()

    val fileReader = FileReader(csvFilePath)
    val csvReader = CSVReader(fileReader)

    // Leer las líneas del archivo CSV y crear objetos Ticket
    var record: Array<String>?
    csvReader.readNext() // Saltar la primera fila si es un encabezado
    while (csvReader.readNext().also { record = it } != null) {
        val ticket = Ticket(
            ticketNumber = record!![0].toInt(),
            originCityIataCode = record!![1],
            destinationCityIataCode = record!![2],
            originLatitude = record!![3].toDouble(),
            originLongitude = record!![4].toDouble(),
            destinationLatitude = record!![5].toDouble(),
            destinationLongitude = record!![6].toDouble()
        )
        tickets.add(ticket)
    }

    csvReader.close()
    fileReader.close()

    return tickets
}

fun main() {
    val csvFilePath = "dataset2.csv"

    try {
        val fileReader = FileReader(csvFilePath)
        val csvReader = CSVReader(fileReader)

        // Leer las líneas del archivo CSV
        var record: Array<String>?
        while (csvReader.readNext().also { record = it } != null) {
            // Procesar cada línea del archivo
            for (column in record!!) {
                println(column) //  realizar cualquier operación que se necesite aquí
            }
        }

        csvReader.close()
        fileReader.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

}
