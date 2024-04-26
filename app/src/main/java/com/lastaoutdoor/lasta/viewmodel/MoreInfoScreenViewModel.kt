package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lastaoutdoor.lasta.data.model.activity.Activity
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.activity.Difficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreInfoScreenViewModel @Inject constructor() : ViewModel() {
  /* Just a default activity to fill in the mutable state*/
  private val dummyActivity =
      Activity(ActivityType.CLIMBING, Difficulty.EASY, 1.5f, "3 hours", "Test Title")
  val activityToDisplay = mutableStateOf(dummyActivity)

  /*Changes the int difficulty of the activity for its String equivalent : 0 -> Easy, 1 -> Medium, etc...*/
  fun processDiffText(outdoorActivity: Activity): String {
    return when (outdoorActivity.difficulty) {
      Difficulty.EASY -> "Easy"
      Difficulty.NORMAL -> "Normal"
      Difficulty.HARD -> "Hard"
    }
  }

  fun changeActivityToDisplay(outdoorActivity: Activity) {
    activityToDisplay.value = outdoorActivity
  }
}
