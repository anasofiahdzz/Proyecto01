package datas

/**
 * Data class que representa los datos del ticket obtenido de los archivos csv
 *
 * @property num_ticket numero de ticket buscado con 16 caracteres
 * @property origin IATA de origen del viajero
 * @property destination IATA de destino del viajero
 * @property origin_latitude latitud de origen del viajero
 * @property origin_longitude longitud de origen del viajero
 * @property destination_latitude latitud de destino del viajero
 * @property destination_longitude longitud de destino del viajero
 */
data class Ticket(
    val num_ticket : String,
    val origin : String,
    val destination : String,
    val origin_latitude : String,
    val origin_longitude : String,
    val destination_latitude : String,
    val destination_longitude : String,
)
