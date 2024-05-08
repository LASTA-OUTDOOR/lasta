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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.activity.Rating
import com.lastaoutdoor.lasta.models.map.MapItinerary
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.ui.components.WeatherReportBig
import com.lastaoutdoor.lasta.ui.screen.map.MapScreen
import com.lastaoutdoor.lasta.ui.theme.Black
import com.lastaoutdoor.lasta.ui.theme.GreenDifficulty
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue
import com.lastaoutdoor.lasta.ui.theme.RedDifficulty
import com.lastaoutdoor.lasta.ui.theme.YellowDifficulty
import com.lastaoutdoor.lasta.viewmodel.MapState

// MoreInfoScreen : displays all the information of an activity
@Composable
fun MoreInfoScreen(
    activityToDisplay: Activity,
    state: MapState,
    updatePermission: (Boolean) -> Unit,
    initialPosition: LatLng,
    initialZoom: Float,
    updateMarkers: (LatLng, Double) -> Unit,
    updateSelectedMarker: (Marker?) -> Unit,
    clearSelectedItinerary: () -> Unit,
    selectedZoom: Float,
    goToMarker: (Activity) -> Marker,
    weather: WeatherResponse?,
    markerList: List<Marker>,
    selectedItinerary: MapItinerary?,
    navigateBack: () -> Unit,
    setWeatherBackToUserLoc: () -> Unit
) {
  var isMapDisplayed = remember { mutableStateOf(false) }
  if (!isMapDisplayed.value) {
    Column(modifier = Modifier.fillMaxSize().testTag("MoreInfoComposable")) {
      LazyColumn(modifier = Modifier.weight(1f).padding(8.dp)) {
        item { Spacer(modifier = Modifier.height(15.dp)) }
        // contains the top icon buttons
        item {
          TopBar {
            navigateBack()
            setWeatherBackToUserLoc()
          }
        }
        // displays activity title and duration
        item { ActivityTitleZone(activityToDisplay) }
        item {
          WeatherReportBig(weather, true)
        } // displays activity difficulty, ration and view on map button
        item { MiddleZone(activityToDisplay, isMapDisplayed) }
        // filled with a spacer for the moment but will contain address + community
      }
      StartButton()
    }
  } else {
    Column(modifier = Modifier.fillMaxSize().testTag("MoreInfoMap")) {
      val marker = goToMarker(activityToDisplay)
      TopBar {
        navigateBack()
        setWeatherBackToUserLoc()
      }
      MapScreen(
          state,
          updatePermission,
          initialPosition,
          initialZoom,
          updateMarkers,
          updateSelectedMarker,
          clearSelectedItinerary,
          selectedZoom,
          marker,
          selectedItinerary,
          markerList,
      ) {
        clearSelectedItinerary()
      }
      updateSelectedMarker(marker)
    }
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
fun MiddleZone(activityToDisplay: Activity, isMapDisplayed: MutableState<Boolean>) {
  Row(modifier = Modifier.fillMaxWidth().testTag("MoreInfoMiddleZone")) {
    DiffAndRating(activityToDisplay)
    Spacer(Modifier.weight(1f))
    ViewOnMapButton(isMapDisplayed)
  }
}

// Button to view the activity on the map
@Composable
fun ViewOnMapButton(isMapDisplayed: MutableState<Boolean>) {
  Column(
      modifier = Modifier.padding(vertical = 25.dp).testTag("viewOnMapButton"),
      horizontalAlignment = Alignment.End) {
        ElevatedButton(
            onClick = { isMapDisplayed.value = true },
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
fun DiffAndRating(activityToDisplay: Activity) {
  Column(modifier = Modifier.padding(vertical = 5.dp)) {
    RatingDisplay(activityToDisplay.rating, activityToDisplay.numRatings)
  }
}

// Displays the rating of the activity
@Composable
fun RatingDisplay(rating: Float, numRating: Int) {
  Row(
      modifier = Modifier.padding(vertical = 30.dp),
      verticalAlignment = Alignment.CenterVertically) {
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
        for (i in 1..5) {
          // computes the correct star filling to use and then generate it
          val id =
              if (rating >= i) R.drawable.filled_star
              else if (rating >= (i - 0.5)) R.drawable.semifilled_star else R.drawable.empty_star
          Star(id)
        }
        Text(
            modifier = Modifier.padding(vertical = 2.dp, horizontal = 2.dp),
            text = "($numRating)",
            style =
                TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight(500),
                    letterSpacing = 0.15.sp,
                ))
      }
}

@Composable
fun Star(iconId: Int) {
  Icon(
      painter = painterResource(iconId),
      contentDescription = "Rating Star",
      tint = PrimaryBlue,
      modifier = Modifier.width(17.dp).height(16.dp))
}

// Displays the difficulty of the activity
@Composable
fun ElevatedDifficultyDisplay(activityToDisplay: Activity) {
  val difficultyColor =
      when (activityToDisplay.difficulty) {
        Difficulty.EASY -> GreenDifficulty
        Difficulty.NORMAL -> YellowDifficulty
        Difficulty.HARD -> RedDifficulty
      }
  ElevatedButton(
      onClick = { /*TODO let user change activity difficulty */},
      contentPadding = PaddingValues(all = 3.dp),
      modifier = Modifier.width(80.dp).height(24.dp).testTag("elevatedTestTag"),
      colors = ButtonDefaults.buttonColors(containerColor = difficultyColor)) {
        Text(
            activityToDisplay.difficulty.toString(),
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
fun TopBarLogo(logoPainterId: Int, isFriendProf: Boolean = false, f: () -> Unit) {
  IconButton(modifier = Modifier.testTag("TopBarLogo"), onClick = { f() }) {
    Icon(
        painter = painterResource(id = logoPainterId),
        contentDescription = "Top Bar logo $logoPainterId",
        modifier = Modifier.width(26.dp).height(26.dp),
        // put to white if bool else put to default color
        tint = if (isFriendProf) Color.White else MaterialTheme.colorScheme.onSurface)
  }
}

// Displays the title of the activity, its type and its duration
@Composable
fun ActivityTitleZone(activityToDisplay: Activity) {
  Row { ElevatedActivityType(activityToDisplay) }
  Row {
    ActivityPicture()
    ActivityTitleText(activityToDisplay)
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
      ElevatedDifficultyDisplay(activityToDisplay)
    }
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

@Composable
fun AddRatingButton() {

}

@Composable
fun RatingSection(ratings: List<Rating> = emptyList()) {

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        RatingInput()
        RatingList(ratings)
    }

}

@Composable
fun RatingList(ratings: List<Rating>) {


}


@Composable
fun RatingInput() {

}

