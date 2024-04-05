package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import com.lastaoutdoor.lasta.repositories.ActivitiesRepository
import javax.inject.Inject

class RecentActivitiesViewModel @Inject constructor(private val repository: ActivitiesRepository) :
    ViewModel() {}
