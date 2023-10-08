package datos

/**
 * Data class que representa los elementos en los códigos de aeropuertos
 * @property ident identificador del código de aeropuerto
 * @property type tipo de de aeropuerto (grande, pequeño, helipuerto, etc)
 * @property name nombre del aeropuerto
 * @property elevation_ft elevación respecto al nivel del mar
 * @property continent continente del aeropuerto
 * @property iso_country pais donde reside el aeropuerto
 * @property iso_region región o estado donde pertenece el aeropuerto
 * @property municipality minucipio del aeropuerto
 * @property gps_code código gps del aeropuerto
 * @property iata_code código IATA del aeropuerto
 * @property local_code códgio local del aeropuerto
 * @property coordinates coordenadas de donde reside el aeropuerto (longitud y latitud)
 */
data class CodigosIATA(
    val ident : String,
    val type : String,
    val name : String,
    val elevation_ft : String,
    val continent : String,
    val iso_country : String,
    val iso_region : String,
    val municipality : String,
    val gps_code : String,
    val iata_code : String,
    val local_code : String,
    val coordinates : String,
)
