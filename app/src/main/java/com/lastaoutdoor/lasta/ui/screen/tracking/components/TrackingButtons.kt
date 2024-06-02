package com.lastaoutdoor.lasta.ui.screen.tracking.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.services.StopwatchState
import com.lastaoutdoor.lasta.ui.theme.RedDifficulty

@Composable
fun TrackingButtons(
    currentState: StopwatchState,
    cancelEnabled: Boolean,
    onClickStart: () -> Unit,
    onClickCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
  Row(
      modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
      verticalAlignment = Alignment.Top,
      horizontalArrangement = Arrangement.SpaceEvenly) {
        Button(
            modifier = Modifier.fillMaxHeight(0.8f).weight(0.45f),
            onClick = onClickStart,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        if (currentState == StopwatchState.Started) RedDifficulty
                        else MaterialTheme.colorScheme.primary,
                    contentColor = Color.White)) {
              Text(
                  text =
                      if (currentState == StopwatchState.Started)
                          LocalContext.current.getString(R.string.tracking_pause_btn)
                      else if ((currentState == StopwatchState.Stopped))
                          LocalContext.current.getString(R.string.tracking_resume_btn)
                      else LocalContext.current.getString(R.string.tracking_start_btn))
            }
        Spacer(modifier = Modifier.weight(0.1f))
        Button(
            modifier = Modifier.fillMaxHeight(0.8f).weight(0.45f),
            onClick = onClickCancel,
            enabled = cancelEnabled,
            colors = ButtonDefaults.buttonColors(Color.Gray)) {
              Text(text = LocalContext.current.getString(R.string.tracking_finish_btn))
            }
      }
}
