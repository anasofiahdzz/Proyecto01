package com.sodiri.tamarweather

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cafsoft.foundation.HTTPURLResponse
import cafsoft.foundation.URLComponents
import cafsoft.foundation.URLQueryItem
import cafsoft.foundation.URLSession
import java.net.URL

class Weather (private var theAPIKey: String){
    fun requestWeatherData(cityName: String, countryISOCode: String, delegate: OnDataResponse) {
        val components = URLComponents()
        components.scheme = "https"
        components.host = "api.openweathermap.org"
        components.path = "/data/2.5/weather"
        components.queryItems = arrayOf(
            URLQueryItem("appid", theAPIKey),
            URLQueryItem("units", "metric"),
            URLQueryItem("lang", "es"),
            //URLQueryItem("q", "$cityName,$countryISOCode")
        )

        val url = components.url
        URLSession.getShared().dataTask(url) { data, response, error ->
            val resp = response as? HTTPURLResponse
            var root: Root? = null
            var statusCode = -1
            if (error == null && resp?.statusCode == 200) {
                val text = data.toText()
                val gsonBuilder = GsonBuilder()
                gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                val gson = gsonBuilder.create()
                root = gson.fromJson(text, Root::class.java)
                statusCode = resp.statusCode
            }
            delegate.onChange(error != null, statusCode, root)
        }.resume()
    }

    interface OnDataResponse {
        fun onChange(isNetworkError: Boolean, statusCode: Int, root: Root?)
    }
}