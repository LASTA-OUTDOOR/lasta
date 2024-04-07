package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import com.lastaoutdoor.lasta.repository.ActivitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentActivitiesViewModel @Inject constructor(private val repository: ActivitiesRepository) :
    ViewModel() {}
