package com.sodiri.tamarweather.dataclimas

import com.sodiri.tamarweather.dataclimas.ClimaResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz para la obtención del clima
 */
interface ClimaApi {

    /**
     * Función que para llamar a la Api
     * con los datos de ClimaResponse en forma de lista
     */
    @GET("data/2.5/weather")
    fun obtenerClima(
        @Query("q") ciudad : String,
        @Query("appid") llaveApi: String
    ) : Call<List<ClimaResponse>>

}