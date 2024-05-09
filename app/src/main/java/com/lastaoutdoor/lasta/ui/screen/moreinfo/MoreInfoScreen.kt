package com.lastaoutdoor.lasta.ui.screen.moreinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.activity.Rating
import com.lastaoutdoor.lasta.models.map.MapItinerary
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.components.SeparatorComponent
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
    usersList: List<UserModel?>,
    getUserModels: (List<String>) -> Unit,
    ratings: List<Rating>,
    writeNewRating: (String, Rating, String) -> Unit,
    currentUser: UserModel?,
    weather: WeatherResponse?,
    markerList: List<Marker>,
    selectedItinerary: MapItinerary?,
    navigateBack: () -> Unit,
    setWeatherBackToUserLoc: () -> Unit
) {
  val isMapDisplayed = remember { mutableStateOf(false) }
  val isReviewing = remember { mutableStateOf(false) }
  val text = remember { mutableStateOf("") }
  if (!isMapDisplayed.value) {
    Column(
        modifier = Modifier.fillMaxSize(1f).testTag("MoreInfoComposable"),
        verticalArrangement = Arrangement.SpaceBetween) {
          Column(modifier = Modifier.padding(8.dp)) {
            Spacer(modifier = Modifier.height(15.dp))
            // contains the top icon buttons
            TopBar {
              navigateBack()
              setWeatherBackToUserLoc()
            }
            // displays activity title and duration
            ActivityTitleZone(activityToDisplay)
            WeatherReportBig(weather, true)
            // displays activity difficulty, ration and view on map button
            MiddleZone(
                activityToDisplay,
                isMapDisplayed,
                isReviewing,
                text,
                ratings,
                writeNewRating,
                currentUser)
            // filled with a spacer for the moment but will contain address + community
          }
          Column(
              modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                LazyColumn(modifier = Modifier.weight(0.88f)) {
                  item { RatingCards(activityToDisplay.ratings, usersList, getUserModels) }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Box(modifier = Modifier.weight(0.12f), contentAlignment = Alignment.Center) {
                  StartButton()
                }
              }
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
fun MiddleZone(
    activityToDisplay: Activity,
    isMapDisplayed: MutableState<Boolean>,
    isReviewing: MutableState<Boolean>,
    text: MutableState<String>,
    ratings: List<Rating>,
    writeNewRating: (String, Rating, String) -> Unit,
    currentUser: UserModel?
) {
  Row(
      modifier = Modifier.fillMaxWidth().testTag("MoreInfoMiddleZone"),
      horizontalArrangement = Arrangement.SpaceBetween) {
        RatingLine(activityToDisplay, isReviewing, text, ratings, writeNewRating, currentUser)
        ViewOnMapButton(isMapDisplayed)
      }
  SeparatorComponent()
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

/** Displays the rating of the activity and the "Add review" button */
@Composable
fun RatingLine(
    activityToDisplay: Activity,
    isReviewing: MutableState<Boolean>,
    text: MutableState<String>,
    ratings: List<Rating>,
    writeNewRating: (String, Rating, String) -> Unit,
    currentUser: UserModel?
) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    DiffAndRating(activityToDisplay = activityToDisplay)
    Spacer(modifier = Modifier.padding(6.dp))
    AddRatingButton(
        activityToDisplay,
        { isReviewing.value = true },
        isReviewing,
        text,
        ratings,
        writeNewRating,
        currentUser)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRatingButton(
    activityToDisplay: Activity,
    onShowReviewModal: () -> Unit,
    isReviewing: MutableState<Boolean>,
    text: MutableState<String>,
    ratings: List<Rating>,
    writeNewRating: (String, Rating, String) -> Unit,
    currentUser: UserModel?
) {
  if (isReviewing.value) {
    ModalBottomSheet(
        onDismissRequest = { isReviewing.value = false }, modifier = Modifier.fillMaxWidth()) {
          Column(
              modifier = Modifier.padding(30.dp),
              horizontalAlignment = Alignment.CenterHorizontally) {
                val selectedStarCount = remember { mutableIntStateOf(1) }
                Text(
                    text = "Review: ${activityToDisplay.name}",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically) {
                      Text(
                          text = "Please rate (out of 5 stars): ",
                          style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium))
                      StarButtons(selectedStarCount = selectedStarCount)
                    }

                OutlinedTextField(
                    value = text.value,
                    label = { Text(text = "Write your comment here:") },
                    onValueChange = { text.value = it },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth())

                // Publish button
                ElevatedButton(
                    onClick = {
                      println("Ratings: ${activityToDisplay.ratings}")
                      var newMeanRating =
                          activityToDisplay.ratings.sumOf { it.rating.toInt() } +
                              selectedStarCount.intValue
                      println("New mean rating: $newMeanRating")
                      val division =
                          newMeanRating.toDouble() / (activityToDisplay.ratings.size + 1.0)
                      println("Division : $division")
                      val string = String.format("%.1f", division)
                      println("String : $string")

                      if (currentUser != null) {
                        writeNewRating(
                            activityToDisplay.activityId,
                            Rating(
                                currentUser.userId,
                                text.value,
                                selectedStarCount.intValue.toString()),
                            string)
                      }
                      isReviewing.value = false
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue, contentColor = Color.White)) {
                      Text(
                          text = "Publish!",
                          style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
                    }
              }
        }
  } else {
    IconButton(
        onClick = { onShowReviewModal() },
        colors =
            IconButtonColors(
                containerColor = Color.Yellow,
                contentColor = Color.Black,
                disabledContentColor = Color.Yellow,
                disabledContainerColor = Color.Black),
        modifier = Modifier.size(25.dp)) {
          Icon(
              painter = painterResource(id = R.drawable.plus),
              contentDescription = "Add Rating",
              modifier = Modifier.width(16.dp).height(16.dp),
              tint = Color.Black,
          )
        }
  }
}

@Composable
fun StarButtons(selectedStarCount: MutableState<Int>) {
  Row(modifier = Modifier.padding(2.dp), verticalAlignment = Alignment.CenterVertically) {
    for (i in 1..5) {
      val isSelected = i <= selectedStarCount.value
      IconButton(onClick = { selectedStarCount.value = i }, modifier = Modifier.size(25.dp)) {
        Icon(
            painter =
                painterResource(
                    id = if (isSelected) R.drawable.filled_star else R.drawable.empty_star),
            contentDescription = "Star $i",
            modifier = Modifier.width(16.dp).height(16.dp),
            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Black)
      }
    }
  }
}

@Composable
fun RatingCard(rating: Rating, usersList: List<UserModel?>) {
  Card(
      modifier =
          Modifier.fillMaxWidth()
              .padding(vertical = 8.dp, horizontal = 16.dp)
              .clickable(onClick = {})
              .testTag("/* TODO */"),
      shape = RoundedCornerShape(8.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surface,
              contentColor = MaterialTheme.colorScheme.onSurface,
              disabledContainerColor = MaterialTheme.colorScheme.surface,
              disabledContentColor = MaterialTheme.colorScheme.onSurface)) {
        Column(modifier = Modifier.padding(10.dp)) {
          // Row that with user profile picture, user name and rating
          Row {
            AsyncImage(
                model = usersList.find { it?.userId == rating.userId }?.profilePictureUrl,
                contentDescription = "Profile picture",
                modifier =
                    Modifier.size(30.dp)
                        .clip(CircleShape)
                        .border(
                            2.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(100.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.default_profile_icon))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = usersList.find { it?.userId == rating.userId }?.userName ?: "Unknown",
                style =
                    TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(500),
                        letterSpacing = 0.15.sp,
                    ),
                modifier = Modifier.padding(5.dp))
            Spacer(modifier = Modifier.weight(1f))
            // Row composed of the rating as Int, and the Star
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                  text = rating.rating,
                  style =
                      TextStyle(
                          fontSize = 16.sp,
                          lineHeight = 24.sp,
                          fontWeight = FontWeight.Bold,
                          letterSpacing = 0.15.sp,
                      ),
                  modifier = Modifier.padding(3.dp))
              Star(R.drawable.filled_star)
            }
          }
          SeparatorComponent()
          // Row with the comment
          Row {
            Text(
                text = rating.comment,
                style =
                    TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight(400),
                        color = Color.Gray,
                        letterSpacing = 0.1.sp,
                    ),
                modifier = Modifier.padding(5.dp))
          }
        }
      }
}

@Composable
fun RatingCards(
    ratings: List<Rating>,
    usersList: List<UserModel?>,
    getUserModels: (List<String>) -> Unit
) {
  val userIds = ratings.map { it.userId }
  getUserModels(userIds)
  for (rating in ratings.reversed()) {
    RatingCard(rating, usersList)
  }
}
