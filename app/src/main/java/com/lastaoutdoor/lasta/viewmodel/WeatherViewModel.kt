package com.lastaoutdoor.lasta.viewmodel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lastaoutdoor.lasta.data.api.weather.WeatherForecast
import com.lastaoutdoor.lasta.data.api.weather.WeatherForecastResponse
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.repository.api.WeatherRepository
import com.lastaoutdoor.lasta.repository.app.ConnectivityRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.utils.ErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltViewModel
class WeatherViewModel
@Inject
constructor(
    private val weatherRepository: WeatherRepository,
    application: Application,
    private val errorToast: ErrorToast,
    private val connectivityRepositoryImpl: ConnectivityRepository,
) : AndroidViewModel(application) {
  val isConnected =
      connectivityRepositoryImpl.connectionState.stateIn(
          initialValue = ConnectionState.OFFLINE,
          scope = viewModelScope,
          started = SharingStarted.WhileSubscribed(5000))
  private val fusedLocationClient: FusedLocationProviderClient by lazy {
    LocationServices.getFusedLocationProviderClient(application)
  }

  private val _weather = MutableLiveData<WeatherResponse>()
  val weather: LiveData<WeatherResponse> = _weather

  private val _weatherForecast = MutableLiveData<WeatherForecastResponse>()
  val weatherForecast: LiveData<WeatherForecastResponse> = _weatherForecast

  init {
    fetchWeatherWithUserLoc()
  }

  fun fetchWeatherWithUserLoc() {
    if (ContextCompat.checkSelfPermission(
        getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED) {
      viewModelScope.launch {

        // Call surrounded by try-catch block to make handle exceptions caused by Weather API
        try {
          isConnected.collect {
            if (it == ConnectionState.CONNECTED) {
              val location = fusedLocationClient.lastLocation.await()
              val weather =
                  weatherRepository.getWeatherWithLoc(location.latitude, location.longitude)
              _weather.postValue(weather)
            }
          }
        } catch (e: Exception) {
          errorToast.showToast(ErrorType.ERROR_WEATHER)
        }
      }
    }
  }

  fun changeLocOfWeather(a: Activity) {
    viewModelScope.launch {

      // Call surrounded by try-catch block to make handle exceptions caused by Weather API
      try {
        // current weather for activity location
        isConnected.collect {
          if (it == ConnectionState.CONNECTED) {
            val weather =
                weatherRepository.getWeatherWithLoc(a.startPosition.lat, a.startPosition.lon)
            _weather.postValue(weather)
            // forecast for activity location
            val weatherForecast =
                weatherRepository.getForecastWeather(a.startPosition.lat, a.startPosition.lon)
            _weatherForecast.postValue(weatherForecast)
          }
        }
      } catch (e: Exception) {
        errorToast.showToast(ErrorType.ERROR_WEATHER)
      }
    }
  }

  fun getForecast(): WeatherForecast? {
    if (_weatherForecast.value == null) return null
    // get the weather forecast for 24h from now
    // since there is a forecast every 3 hours and counting the previous forecasts not yet removed,
    // we need to take 9th forecast in the list
    // to get approximately 24h from now
    return _weatherForecast.value!!.list[8]
  }

  // helper function to convert unix date format to a smoother string
  @Composable
  fun getForecastDate(): String {
    if (_weatherForecast.value == null) return ""
    val resp = _weatherForecast.value!!.list[8]
    return "${resp.dt.substring(11, 16)}h"
  }
}
