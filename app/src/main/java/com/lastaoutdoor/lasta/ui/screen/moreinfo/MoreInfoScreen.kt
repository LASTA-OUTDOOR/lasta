package com.lastaoutdoor.lasta.ui.screen.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.activity.OutdoorActivity
import com.lastaoutdoor.lasta.ui.theme.Black
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue
import com.lastaoutdoor.lasta.ui.theme.YellowDifficulty

@Composable
fun MoreInfoScreen(activity: OutdoorActivity, navController: NavController) {
  LazyColumn(modifier = Modifier.padding(8.dp)) {
    item { Spacer(modifier = Modifier.height(15.dp)) }
    // contains the top icon buttons
    item { TopBar(navController) }
    // displays activity title and duration
    item { ActivityTitleZone(activity = activity) }
    // displays activity difficulty, ration and view on map button
    item { MiddleZone(activity) }
    // filled with a spacer for the moment but will contain address + community
    item { Spacer(modifier = Modifier.height(350.dp)) }
    // Bottom start activity button
    item { StartButton() }
  }
}

@Composable
fun StartButton() {
  Row(
      modifier = Modifier.fillMaxWidth().testTag("Start button"),
      horizontalArrangement = Arrangement.Center) {
        ElevatedButton(
            onClick = {
              /** TODO : Start Activity */
            },
            modifier = Modifier.width(305.dp).height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
              Text(
                  "Start",
                  style =
                      TextStyle(
                          fontSize = 22.sp,
                          lineHeight = 28.sp,
                          fontWeight = FontWeight(400),
                      ))
            }
      }
}

@Composable
fun MiddleZone(activity: OutdoorActivity) {
  Row {
    DiffAndRating(activity)
    Spacer(Modifier.width(170.dp))
    ViewOnMapButton()
  }
}

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
              "View On Map",
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

@Composable
fun DiffAndRating(activity: OutdoorActivity) {
  Column(modifier = Modifier.padding(vertical = 5.dp)) {
    ElevatedDifficultyDisplay(diff = fetchDiffText(activity))
    RatingDisplay(4.3)
  }
}

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
                color = Black,
                letterSpacing = 0.15.sp,
            ))
  }
}

// intern helper function, WILL BE MOVED TO VIEWMODEL
fun fetchDiffText(activity: OutdoorActivity): String {
  return when (activity.difficulty) {
    0 -> "Easy"
    1 -> "Medium"
    2 -> "Difficult"
    else -> {
      "No available difficulty"
    }
  }
}

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

@Composable
fun TopBar(navController: NavController) {
  Row(modifier = Modifier.fillMaxWidth()) {
    TopBarLogo(R.drawable.arrow_back) { navController.navigateUp() }
    Spacer(modifier = Modifier.width(180.dp))
    TopBarLogo(R.drawable.download_button) {}
    TopBarLogo(R.drawable.share) {}
    TopBarLogo(R.drawable.favourite) {}
  }
}

@Composable
fun TopBarLogo(logoPainterId: Int, f: () -> Unit) {
  IconButton(onClick = { f() }) {
    Icon(
        painter = painterResource(id = logoPainterId),
        contentDescription = "Top Bar logo",
        modifier = Modifier.width(26.dp).height(26.dp))
  }
}

@Composable
fun ActivityTitleZone(activity: OutdoorActivity) {
  Row { ElevatedActivityType(activity) }
  Row {
    ActivityPicture()
    ActivityTitleText(activity)
  }
}

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
fun ActivityTitleText(activity: OutdoorActivity) {
  Column(modifier = Modifier.padding(vertical = 25.dp, horizontal = 5.dp)) {
    Text(
        text = activity.locationName ?: "No Title",
        style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight(600), color = Black))
    Text(
        text = activity.duration ?: "No Duration",
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
fun ElevatedActivityType(activity: OutdoorActivity) {
  ElevatedButton(
      onClick = {},
      contentPadding = PaddingValues(all = 3.dp),
      modifier = Modifier.padding(3.dp).width(64.dp).height(20.dp),
      colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
        Text(
            activity.getActivityType().toString().replaceFirstChar { it.uppercase() },
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
