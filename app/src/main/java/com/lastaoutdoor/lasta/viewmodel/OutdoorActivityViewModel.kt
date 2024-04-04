package com.lastaoutdoor.lasta.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lastaoutdoor.lasta.model.repository.OutdoorActivityRepository

class OutdoorActivityViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: OutdoorActivityRepository by lazy {
    OutdoorActivityRepository(/*application*/ )
  }

  fun refresh() {}

  fun filter() {}
}
