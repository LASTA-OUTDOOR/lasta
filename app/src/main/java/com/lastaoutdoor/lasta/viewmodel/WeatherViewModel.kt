package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lastaoutdoor.lasta.data.api.WeatherApi
import com.lastaoutdoor.lasta.data.api.WeatherResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
  private val _weather = MutableLiveData<WeatherResponse>()
  val weather: LiveData<WeatherResponse> = _weather

  init {
    fetchWeather()
  }

  private fun fetchWeather() {
    CoroutineScope(Dispatchers.IO).launch {
      try {
        val weather = WeatherApi.getCurrentWeather("Lausanne")
        _weather.postValue(weather)
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }
}
