package com.lastaoutdoor.lasta.view.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.viewmodel.RecentActivitiesViewModel
import com.lastaoutdoor.lasta.viewmodel.StatisticsViewModel

@Composable
fun ProfileScreen(
    statisticsViewModel: StatisticsViewModel,
    recentActivitiesViewModel: RecentActivitiesViewModel
) {

  var text by remember { mutableStateOf("Add a few words about yourself") }
  var sport by remember { mutableStateOf(false) }
  var timeFrame by remember { mutableStateOf(false) }

  Column {
    Row(modifier = Modifier.fillMaxWidth().padding(15.dp)) {
      Image(
          painter = painterResource(id = R.drawable.pov_img),
          contentDescription = null,
          modifier =
              Modifier
                  // Set image size to 80 dp
                  .size(80.dp)
                  // Clip image to be shaped as a circle
                  .clip(CircleShape)
                  // Add a border with a 1.5 dp width and the primary color
                  .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape))

      // Add a horizontal space between the image and the column
      Spacer(modifier = Modifier.width(8.dp))

      Column {
        Text("Scrum Master")

        // Add a vertical space between the author and message texts
        Spacer(modifier = Modifier.height(4.dp))

        Text("Bio")

        // Add a vertical space between the author and message texts
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            modifier = Modifier.border(1.dp, Color.Black, RoundedCornerShape(4.dp)).padding(8.dp),
            singleLine = false,
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            maxLines = 8 // Adjust maxLines as needed
            )
      }
    }

    Row {
      Text("Activity")

      Sports(
          sportsName = "Select a sport",
          dropDownItems =
              listOf(
                  Sport("Hiking"),
                  Sport("Biking"),
                  Sport("Skiiing"),
              ),
          onItemClick = {
            /*TODO*/
          })
    }

    // Add a vertical space between the author and message texts
    Spacer(modifier = Modifier.height(4.dp))

    TimeFrameSelector()

    // Add a vertical space between the author and message texts
    Spacer(modifier = Modifier.height(4.dp))

    // Bar graph layout
    Column(
        modifier = Modifier.padding(horizontal = 30.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

          // Bar graph x and y data
          val ordinate = mutableListOf(5, 10, 15, 0, 5, 7, 11)
          val abscissa = WeekDay.values().toList() // needs to be an enum

          val ordinateFloat: List<Float> = ordinate.map { it.toFloat() / ordinate.max() }

          BarGraph(
              graphBarData = ordinateFloat,
              xAxisScaleData = abscissa,
              barData_ = ordinate,
              height = 300.dp,
              roundType = BarType.TOP_CURVED,
              barWidth = 20.dp,
              barColor = MaterialTheme.colorScheme.primary,
              barArrangement = Arrangement.SpaceEvenly)
        }
  }

  // Add a horizontal space between the image and the column
  Spacer(modifier = Modifier.width(8.dp))

  // Recent activities layout
  LazyVerticalGrid(modifier = Modifier, columns = GridCells.Adaptive(100.dp)) {}
}
