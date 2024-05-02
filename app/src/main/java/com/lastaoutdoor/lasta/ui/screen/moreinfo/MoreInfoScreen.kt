package com.lastaoutdoor.lasta.ui.screen.moreinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.ui.components.WeatherReportBig
import com.lastaoutdoor.lasta.ui.theme.Black
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue
import com.lastaoutdoor.lasta.ui.theme.YellowDifficulty

// MoreInfoScreen : displays all the information of an activity
@Composable
fun MoreInfoScreen(
    activityToDisplay: Activity,
    processDiffText: (Activity) -> String,
    weather: WeatherResponse?,
    navigateBack: () -> Unit
) {
  Column(modifier = Modifier.fillMaxSize().testTag("MoreInfoComposable")) {
    LazyColumn(modifier = Modifier.weight(1f).padding(8.dp)) {
      item { Spacer(modifier = Modifier.height(15.dp)) }
      // contains the top icon buttons
      item { TopBar(navigateBack) }
      // displays activity title and duration
      item { ActivityTitleZone(activityToDisplay) }
      item { WeatherReportBig(weather, true) }
      // displays activity difficulty, ration and view on map button
      item { MiddleZone(activityToDisplay, processDiffText) }
      // filled with a spacer for the moment but will contain address + community
    }
    StartButton()
  }
}

// Start button : once clicked, the activity tracking starts
@Composable
fun StartButton() {
  Row(
      modifier = Modifier.fillMaxWidth().testTag("MoreInfoStartButton"),
      horizontalArrangement = Arrangement.Center) {
        ElevatedButton(
            onClick = {
              /** TODO : Start Activity */
            },
            modifier = Modifier.fillMaxWidth(0.8f).height(48.dp), // takes up 80% of the width
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
              Text(
                  LocalContext.current.getString(R.string.start),
                  style =
                      TextStyle(
                          fontSize = 22.sp,
                          lineHeight = 28.sp,
                          fontWeight = FontWeight(400),
                      ))
            }
      }
}

// Displays the difficulty and rating of the activity on the left and a button to view the activity
// on the map on the right
@Composable
fun MiddleZone(activityToDisplay: Activity, processDiffText: (Activity) -> String) {
  Row(modifier = Modifier.fillMaxWidth().testTag("MoreInfoMiddleZone")) {
    DiffAndRating(activityToDisplay, processDiffText)
    Spacer(Modifier.weight(1f))
    ViewOnMapButton()
  }
}

// Button to view the activity on the map
@Composable
fun ViewOnMapButton() {
  Column(modifier = Modifier.padding(vertical = 25.dp), horizontalAlignment = Alignment.End) {
    ElevatedButton(
        onClick = {
          /** TODO : Go to map */
        },
        contentPadding = PaddingValues(all = 3.dp),
        modifier = Modifier.width(130.dp).height(40.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
          Text(
              LocalContext.current.getString(R.string.on_map),
              style =
                  TextStyle(
                      fontSize = 16.sp,
                      lineHeight = 24.sp,
                      fontWeight = FontWeight(500),
                      letterSpacing = 0.15.sp,
                  ))
        }
  }
}

// Displays the difficulty and rating of the activity
@Composable
fun DiffAndRating(activityToDisplay: Activity, processDiffText: (Activity) -> String) {
  Column(modifier = Modifier.padding(vertical = 5.dp)) {
    ElevatedDifficultyDisplay(diff = processDiffText(activityToDisplay))
    /*Not implemented yet so a hard-coded value is returned*/
    RatingDisplay(4.3)
  }
}

// Displays the rating of the activity
@Composable
fun RatingDisplay(rating: Double) {
  Row(modifier = Modifier.padding(vertical = 30.dp)) {
    Icon(
        imageVector = Icons.Filled.Star,
        contentDescription = "Rating Star",
        tint = PrimaryBlue,
    )
    Text(
        modifier = Modifier.padding(vertical = 2.dp, horizontal = 2.dp),
        text = rating.toString(),
        style =
            TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(500),
                letterSpacing = 0.15.sp,
            ))
  }
}

// Displays the difficulty of the activity
@Composable
fun ElevatedDifficultyDisplay(diff: String) {
  ElevatedButton(
      onClick = {},
      contentPadding = PaddingValues(all = 3.dp),
      modifier = Modifier.width(80.dp).height(24.dp),
      colors = ButtonDefaults.buttonColors(containerColor = YellowDifficulty)) {
        Text(
            diff,
            style =
                TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(500),
                    color = Black,
                    letterSpacing = 0.15.sp,
                ))
      }
}

// Top Bar that displays the four clickable logos with distinct usages
@Composable
fun TopBar(navigateBack: () -> Unit) {
  Row(modifier = Modifier.fillMaxWidth().testTag("Top Bar")) {
    TopBarLogo(R.drawable.arrow_back) { navigateBack() }
    Spacer(modifier = Modifier.weight(1f))
    TopBarLogo(R.drawable.download_button) {}
    TopBarLogo(R.drawable.share) {}
    TopBarLogo(R.drawable.favourite) {}
  }
}

// Logo of the top bar
@Composable
fun TopBarLogo(logoPainterId: Int, f: () -> Unit) {
  IconButton(onClick = { f() }, modifier = Modifier.testTag("TopBarLogoTag")) {
    Icon(
        painter = painterResource(id = logoPainterId),
        contentDescription = "Top Bar logo",
        modifier = Modifier.width(26.dp).height(26.dp))
  }
}

// Displays the title of the activity, its type and its duration
@Composable
fun ActivityTitleZone(activityToDisplay: Activity) {
  Row { ElevatedActivityType(activityToDisplay) }
  Row {
    ActivityPicture()
    ActivityTitleText(activityToDisplay)
  }
}

// Displays the picture of the activity
@Composable
fun ActivityPicture() {
  Column {
    Image(
        painter = painterResource(id = R.drawable.ellipse),
        contentDescription = "Soon Activity Picture",
        modifier = Modifier.padding(5.dp).width(70.dp).height(70.dp))
  }
}

@Composable
fun ActivityTitleText(activityToDisplay: Activity) {
  Column(modifier = Modifier.padding(vertical = 25.dp, horizontal = 5.dp)) {
    Text(
        text = activityToDisplay.name,
        style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight(600)))
    Text(
        text = "No Duration",
        style =
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(500),
                color = Color.Gray,
                letterSpacing = 0.1.sp,
            ))
  }
}

@Composable
fun ElevatedActivityType(activityToDisplay: Activity) {
  ElevatedButton(
      onClick = {},
      contentPadding = PaddingValues(all = 3.dp),
      modifier =
          Modifier.padding(3.dp)
              .width(64.dp)
              .height(20.dp)
              .testTag("MoreInfoActivityTypeComposable"),
      colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
        Text(
            text = activityToDisplay.activityType.toString(),
            style =
                TextStyle(
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF000000),
                    letterSpacing = 0.5.sp,
                ))
      }
}
