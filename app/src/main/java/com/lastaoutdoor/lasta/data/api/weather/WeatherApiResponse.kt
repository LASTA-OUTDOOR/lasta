package com.lastaoutdoor.lasta.data.api.weather

import com.google.gson.annotations.SerializedName

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
