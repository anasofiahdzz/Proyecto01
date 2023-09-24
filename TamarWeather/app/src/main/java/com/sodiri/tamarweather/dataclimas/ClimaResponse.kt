package com.sodiri.tamarweather.dataclimas

import com.sodiri.tamarweather.dataclimas.Coord
import com.sodiri.tamarweather.dataclimas.Main
import com.sodiri.tamarweather.dataclimas.Weather

/**
 * Data class con los datos que esperamos de recibir de la API
 */
data class ClimaResponse(
    val base: String,
    val cod: Int,
    val coord: List<Coord>,
    val dt: Int,
    val id: Int,
    val main: List<Main>,
    val name: String,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    )