package com.lastaoutdoor.lasta.data.api.weather

import com.google.gson.annotations.SerializedName
import java.math.RoundingMode

private const val KELVIN_CONST = 273.15

data class WeatherResponse(
    @SerializedName("name") val name: String,
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("wind") val wind: Wind,
)

data class Wind(@SerializedName("speed") val speed: Double)

data class Main(
    @SerializedName("temp") val temp: Double,
    @SerializedName("humidity") val hum: Double
)

data class Weather(
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class WeatherForecastResponse(@SerializedName("list") val list: List<WeatherForecast>)

data class WeatherForecast(
    @SerializedName("main") val main: MainForecast,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("dt_txt") val dt: String
)

data class MainForecast(
    @SerializedName("temp") val temp: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    @SerializedName("humidity") val hum: Double
)

// helper function to convert kelvin to celsius, rounded to two decimals
fun kelvinToCelsius(kelvin: Double): Double {
  return (kelvin - KELVIN_CONST).toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
}
