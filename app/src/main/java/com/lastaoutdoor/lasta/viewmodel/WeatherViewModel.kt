package com.lastaoutdoor.lasta.viewmodel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.repository.api.WeatherRepository
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.utils.ErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltViewModel
class WeatherViewModel
@Inject
constructor(
    private val weatherRepository: WeatherRepository,
    application: Application,
    private val errorToast: ErrorToast
) : AndroidViewModel(application) {

  private val fusedLocationClient: FusedLocationProviderClient by lazy {
    LocationServices.getFusedLocationProviderClient(application)
  }

  private val _weather = MutableLiveData<WeatherResponse>()
  val weather: LiveData<WeatherResponse> = _weather

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
          val location = fusedLocationClient.lastLocation.await()
          val weather = weatherRepository.getWeatherWithLoc(location.latitude, location.longitude)
          _weather.postValue(weather)
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
        val weather = weatherRepository.getWeatherWithLoc(a.startPosition.lat, a.startPosition.lon)
        _weather.postValue(weather)
      } catch (e: Exception) {
        errorToast.showToast(ErrorType.ERROR_WEATHER)
      }
    }
  }
}
