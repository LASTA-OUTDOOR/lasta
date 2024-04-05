package com.lastaoutdoor.lasta.view.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.data.model.TimeFrame

@Composable
fun TimeFrameSelector() {
  val selectedTimeFrame = remember { mutableStateOf(TimeFrame.D) }

  Row(modifier = Modifier.padding(8.dp)) {
    TimeFrame.values().forEach { timeframe ->
      Button(
          onClick = { selectedTimeFrame.value = timeframe },
          colors =
              ButtonDefaults.buttonColors(
                  containerColor =
                      if (selectedTimeFrame.value == timeframe) Color.Gray else Color.LightGray,
                  contentColor = Color.Black),
          shape = RoundedCornerShape(4.dp),
          modifier = Modifier.padding(4.dp)) {
            Text(
                timeframe.name,
                color = if (selectedTimeFrame.value == timeframe) Color.White else Color.Black)
          }
    }
  }
}
