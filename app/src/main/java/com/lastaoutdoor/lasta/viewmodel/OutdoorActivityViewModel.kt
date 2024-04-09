package com.lastaoutdoor.lasta.viewmodel

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.lastaoutdoor.lasta.data.model.OutdoorActivity
import com.lastaoutdoor.lasta.repository.ActivitiesRepository
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class OutdoorActivityViewModel : ViewModel() {

  fun refresh() {}

  fun filter() {}

  var toggleActivityDialog = mutableStateOf(false)

  fun setDialogState(boolean: Boolean){
    toggleActivityDialog.value=boolean
  }

  fun getDialogState() : Boolean{
    return toggleActivityDialog.value
  }
}
