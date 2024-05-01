package com.lastaoutdoor.lasta.data.api.weather

import com.lastaoutdoor.lasta.R

/**Get the weather icon form icon id received from query*/
fun getWeatherIconFromId(id:String):Int{
    return when(id){
        "01d" -> R.drawable.weather_sun
        "02d" -> R.drawable.weather_fewclouds
        "03d" -> R.drawable.weather_clouds
        "04d" -> R.drawable.weather_brokenclouds
        "09d" -> R.drawable.weather_showerrain
        "10d" -> R.drawable.weather_rain
        "11d" -> R.drawable.weather_storm
        "13d" -> R.drawable.weather_snow
        "50d" -> R.drawable.waether_mist
        "01n" -> R.drawable.weather_moon
        "02n" -> R.drawable.weather_nightclouds
        "03n" -> R.drawable.weather_clouds
        "04n" -> R.drawable.weather_brokenclouds
        "09n" -> R.drawable.weather_showerrain
        "10n" -> R.drawable.weather_nightrain
        "11n" -> R.drawable.weather_storm
        "13n" -> R.drawable.weather_snow
        "50n" -> R.drawable.waether_mist
        else-> {
            R.drawable.weather_sun
        }
    }
}