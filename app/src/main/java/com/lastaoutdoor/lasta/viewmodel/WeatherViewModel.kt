package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.data.api.WeatherResponse
import com.lastaoutdoor.lasta.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {
  private val _weather = MutableLiveData<WeatherResponse>()
  val weather: LiveData<WeatherResponse> = _weather

  init {
    fetchWeather()
  }

  private fun fetchWeather() {
    viewModelScope.launch {
      try {
        val weather = weatherRepository.getCurrentWeather("Lausanne")
        _weather.postValue(weather)
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }
}
