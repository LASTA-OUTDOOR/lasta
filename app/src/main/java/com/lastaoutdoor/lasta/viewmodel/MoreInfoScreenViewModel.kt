package com.lastaoutdoor.lasta.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.activity.OutdoorActivity
import com.lastaoutdoor.lasta.repository.ActivitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreInfoScreenViewModel
@Inject
constructor() : ViewModel() {
    /* Just a default activity to fill in the mutable state*/
    private val dummyActivity = OutdoorActivity(ActivityType.CLIMBING, 0, 1.5f, "3 hours", "Test Title")
    val activityToDisplay = mutableStateOf(dummyActivity)

    /*Changes the int difficulty of the activity for its String equivalent*/
    fun processDiffText(outdoorActivity: OutdoorActivity):String{
        return when (outdoorActivity.difficulty) {
            0 -> "Easy"
            1 -> "Medium"
            2 -> "Difficult"
            else -> {
                "No available difficulty"
            }
        }
    }

    fun changeActivityToDisplay(outdoorActivity: OutdoorActivity){
        activityToDisplay.value=outdoorActivity
    }
}