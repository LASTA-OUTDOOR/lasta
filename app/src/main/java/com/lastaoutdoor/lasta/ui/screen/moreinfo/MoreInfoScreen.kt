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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.api.weather.WeatherForecast
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.activity.Rating
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.components.SeparatorComponent
import com.lastaoutdoor.lasta.ui.components.WeatherForecastDisplay
import com.lastaoutdoor.lasta.ui.components.WeatherReportBig
import com.lastaoutdoor.lasta.ui.screen.map.mapScreen
import com.lastaoutdoor.lasta.ui.screen.moreinfo.components.ShareOptionsDialog
import com.lastaoutdoor.lasta.ui.theme.AccentGreen
import com.lastaoutdoor.lasta.ui.theme.Black
import com.lastaoutdoor.lasta.ui.theme.GreenDifficulty
import com.lastaoutdoor.lasta.ui.theme.OrangeDifficulty
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue
import com.lastaoutdoor.lasta.ui.theme.RedDifficulty
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenCallBacks
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenState
import java.util.Locale

// MoreInfoScreen : displays all the information of an activity
@Composable
fun MoreInfoScreen(
    activityToDisplay: Activity,
    discoverScreenState: DiscoverScreenState,
    discoverScreenCallBacks: DiscoverScreenCallBacks,
    goToMarker: (Activity) -> Marker,
    usersList: List<UserModel?>,
    getUserModels: (List<String>) -> Unit,
    writeNewRating: (String, Rating, String) -> Unit,
    updateDifficulty: (String) -> Unit,
    currentUser: UserModel?,
    shareToFriend: (String, String) -> Unit,
    friends: List<UserModel>,
    weather: WeatherResponse?,
    favorites: List<String>,
    weatherForecast: WeatherForecast?,
    dateWeatherForecast: String,
    flipFavorite: (String) -> Unit,
    navigateBack: () -> Unit,
    navigateToTracking: () -> Unit,
    downloadActivity: (Activity) -> Unit,
    isOnline: ConnectionState,
    setWeatherBackToUserLoc: () -> Unit,
) {
  val isMapDisplayed = remember { mutableStateOf(false) }
  val isReviewing = remember { mutableStateOf(false) }
  val text = remember { mutableStateOf("") }
  val weatherDialog = remember { mutableStateOf(false) }

  if (!isMapDisplayed.value) {
    if (weatherDialog.value) {
      Dialog(onDismissRequest = { weatherDialog.value = false }) {
        WeatherForecastDisplay(weatherForecast = weatherForecast, date = dateWeatherForecast)
      }
    }
    ActivityInfoDisplay(
        activityToDisplay,
        currentUser,
        discoverScreenState,
        discoverScreenCallBacks,
        navigateBack,
        setWeatherBackToUserLoc,
        downloadActivity,
        favorites,
        flipFavorite,
        friends,
        shareToFriend,
        weather,
        weatherDialog,
        navigateToTracking,
        updateDifficulty,
        usersList,
        getUserModels,
        isMapDisplayed,
        isReviewing,
        text,
        writeNewRating,
        isOnline)
  } else {
    MoreInfoMap(
        goToMarker,
        discoverScreenCallBacks,
        discoverScreenState,
        activityToDisplay,
        currentUser,
        downloadActivity,
        favorites,
        flipFavorite,
        friends,
        shareToFriend,
        navigateBack,
        setWeatherBackToUserLoc,
        isOnline)
  }
}

@Composable
fun ActivityInfoDisplay(
    activityToDisplay: Activity,
    currentUser: UserModel?,
    discoverScreenState: DiscoverScreenState,
    discoverScreenCallBacks: DiscoverScreenCallBacks,
    navigateBack: () -> Unit,
    setWeatherBackToUserLoc: () -> Unit,
    downloadActivity: (Activity) -> Unit,
    favorites: List<String>,
    flipFavorite: (String) -> Unit,
    friends: List<UserModel>,
    shareToFriend: (String, String) -> Unit,
    weather: WeatherResponse?,
    weatherDialog: MutableState<Boolean>,
    navigateToTracking: () -> Unit,
    updateDifficulty: (String) -> Unit,
    usersList: List<UserModel?>,
    getUserModels: (List<String>) -> Unit,
    isMapDisplayed: MutableState<Boolean>,
    isReviewing: MutableState<Boolean>,
    text: MutableState<String>,
    writeNewRating: (String, Rating, String) -> Unit,
    isOnline: ConnectionState
) {
  Column(
      modifier = Modifier.fillMaxSize().testTag("MoreInfoComposable"),
      verticalArrangement = Arrangement.SpaceBetween) {
        UpperMoreInfo(
            activityToDisplay,
            currentUser,
            discoverScreenState,
            discoverScreenCallBacks,
            navigateBack,
            setWeatherBackToUserLoc,
            downloadActivity,
            favorites,
            flipFavorite,
            friends,
            shareToFriend,
            updateDifficulty,
            isOnline)
        Column {
          WeatherReportBig(weather, true) { weatherDialog.value = true }
          // displays activity difficulty, ration and view on map button
          MiddleZone(
              activityToDisplay,
              isMapDisplayed,
              isReviewing,
              text,
              writeNewRating,
              currentUser,
              isOnline)
        }
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
          LazyColumn(modifier = Modifier.weight(0.85f)) {
            item { RatingCards(activityToDisplay.ratings, usersList, getUserModels) }
          }
          Spacer(modifier = Modifier.height(5.dp))
          Box(modifier = Modifier.weight(0.15f), contentAlignment = Alignment.Center) {
            StartButton(navigateToTracking, isOnline)
          }
        }
      }
}

@Composable
fun UpperMoreInfo(
    activityToDisplay: Activity,
    currentUser: UserModel?,
    discoverScreenState: DiscoverScreenState,
    discoverScreenCallBacks: DiscoverScreenCallBacks,
    navigateBack: () -> Unit,
    setWeatherBackToUserLoc: () -> Unit,
    downloadActivity: (Activity) -> Unit,
    favorites: List<String>,
    flipFavorite: (String) -> Unit,
    friends: List<UserModel>,
    shareToFriend: (String, String) -> Unit,
    updateDifficulty: (String) -> Unit,
    isOnline: ConnectionState
) {
  Box(modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
    if (activityToDisplay.activityImageUrl != "") {
      AsyncImage(
          model = activityToDisplay.activityImageUrl,
          contentDescription = "activity image",
          contentScale = ContentScale.Crop,
          modifier = Modifier.matchParentSize())
    } else {
      Image(
          painter = painterResource(id = R.drawable.default_activity_bg),
          contentDescription = "activity image not found",
          contentScale = ContentScale.Crop,
          modifier = Modifier.matchParentSize().alpha(0.3f))
    }
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
      // contains the top icon buttons
      if (currentUser != null) {
        TopBar(
            activityToDisplay,
            downloadActivity,
            favorites,
            flipFavorite,
            friends,
            shareToFriend,
            {
              discoverScreenCallBacks.fetchActivities()
              navigateBack()
              setWeatherBackToUserLoc()
            },
            isOnline)
      }
      // displays activity title and duration
      ActivityTitleZone(
          activityToDisplay, updateDifficulty, isOnline, discoverScreenState.centerPoint)
    }
  }
}

@Composable
fun MoreInfoMap(
    goToMarker: (Activity) -> Marker,
    discoverScreenCallBacks: DiscoverScreenCallBacks,
    discoverScreenState: DiscoverScreenState,
    activityToDisplay: Activity,
    currentUser: UserModel?,
    downloadActivity: (Activity) -> Unit,
    favorites: List<String>,
    flipFavorite: (String) -> Unit,
    friends: List<UserModel>,
    shareToFriend: (String, String) -> Unit,
    navigateBack: () -> Unit,
    setWeatherBackToUserLoc: () -> Unit,
    isOnline: ConnectionState
) {
  Column(modifier = Modifier.fillMaxSize().testTag("MoreInfoMap")) {
    LaunchedEffect(Unit) {
      val marker = goToMarker(activityToDisplay)
      discoverScreenCallBacks.updateSelectedMarker(marker)
    }

    if (currentUser != null) {
      Column(modifier = Modifier.padding(8.dp)) {
        TopBar(
            activityToDisplay,
            downloadActivity,
            favorites,
            flipFavorite,
            friends,
            shareToFriend,
            {
              discoverScreenCallBacks.clearSelectedMarker()
              discoverScreenCallBacks.clearSelectedItinerary()
              discoverScreenCallBacks.fetchActivities()
              navigateBack()
              setWeatherBackToUserLoc()
            },
            isOnline)
      }
    }
    mapScreen(
        discoverScreenState.mapState,
        discoverScreenState.initialPosition,
        discoverScreenState.initialZoom,
        discoverScreenCallBacks.updateMarkers,
        discoverScreenCallBacks.updateSelectedMarker,
        discoverScreenCallBacks.clearSelectedItinerary,
        discoverScreenState.selectedZoom,
        discoverScreenState.selectedMarker,
        discoverScreenState.selectedItinerary,
        discoverScreenState.markerList,
        discoverScreenCallBacks.clearSelectedMarker)
  }
}

// Start button : once clicked, the activity tracking starts
@Composable
fun StartButton(navigateToTracking: () -> Unit, isOnline: ConnectionState) {
  Row(
      modifier = Modifier.fillMaxWidth().testTag("MoreInfoStartButton"),
      horizontalArrangement = Arrangement.Center) {
        ElevatedButton(
            onClick = { navigateToTracking() },
            modifier = Modifier.fillMaxWidth(0.6f).height(48.dp), // takes up 80% of the width
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
            enabled = isOnline == ConnectionState.CONNECTED) {
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
    writeNewRating: (String, Rating, String) -> Unit,
    currentUser: UserModel?,
    isOnline: ConnectionState
) {
  Row(
      modifier =
          Modifier.fillMaxWidth()
              .testTag("MoreInfoMiddleZone")
              .padding(vertical = 0.dp, horizontal = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween) {
        RatingLine(activityToDisplay, isReviewing, text, writeNewRating, currentUser, isOnline)
        ViewOnMapButton(isMapDisplayed, isOnline)
      }
  SeparatorComponent()
}

// Button to view the activity on the map
@Composable
fun ViewOnMapButton(isMapDisplayed: MutableState<Boolean>, isOnline: ConnectionState) {
  Column(
      modifier = Modifier.padding(vertical = 25.dp).testTag("viewOnMapButton"),
      horizontalAlignment = Alignment.End) {
        ElevatedButton(
            onClick = { isMapDisplayed.value = true },
            contentPadding = PaddingValues(all = 3.dp),
            modifier = Modifier.width(130.dp).height(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
            enabled = isOnline == ConnectionState.CONNECTED) {
              Text(
                  LocalContext.current.getString(R.string.on_map),
                  style =
                      TextStyle(
                          fontSize = if (Locale.getDefault().language == "de") 14.sp else 16.sp,
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
    writeNewRating: (String, Rating, String) -> Unit,
    currentUser: UserModel?,
    isOnline: ConnectionState
) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    DiffAndRating(activityToDisplay = activityToDisplay)
    Spacer(modifier = Modifier.padding(6.dp))
    AddRatingButton(
        activityToDisplay,
        { isReviewing.value = true },
        isReviewing,
        text,
        writeNewRating,
        currentUser,
        isOnline)
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
fun ElevatedDifficultyDisplay(
    activityToDisplay: Activity,
    updateDifficulty: (String) -> Unit,
    isOnline: ConnectionState
) {
  val difficultyColor =
      when (activityToDisplay.difficulty) {
        Difficulty.EASY -> GreenDifficulty
        Difficulty.NORMAL -> OrangeDifficulty
        Difficulty.HARD -> RedDifficulty
      }
  ElevatedButton(
      onClick = { updateDifficulty(activityToDisplay.activityId) },
      contentPadding = PaddingValues(all = 3.dp),
      modifier = Modifier.width(80.dp).height(24.dp).testTag("elevatedTestTag"),
      colors = ButtonDefaults.buttonColors(containerColor = difficultyColor),
      enabled = isOnline == ConnectionState.CONNECTED) {
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
fun TopBar(
    activityToDisplay: Activity,
    downloadActivity: (Activity) -> Unit,
    favorites: List<String>,
    flipFavorite: (String) -> Unit,
    friends: List<UserModel>,
    shareToFriend: (String, String) -> Unit,
    navigateBack: () -> Unit,
    isOnline: ConnectionState
) {
  val openDialog = remember { mutableStateOf(false) }
  ShareOptionsDialog(activityToDisplay, openDialog, friends, shareToFriend)

  Row(modifier = Modifier.fillMaxWidth().testTag("Top Bar")) {
    TopBarLogo(Icons.AutoMirrored.Outlined.ArrowBack, { navigateBack() }, ConnectionState.CONNECTED)
    Spacer(modifier = Modifier.weight(1f))
    TopBarLogo(R.drawable.download_button, false, { downloadActivity(activityToDisplay) }, isOnline)
    TopBarLogo(Icons.Outlined.Share, { openDialog.value = true }, isOnline)
    // if activity is in favorites, display the filled heart, else display the empty heart
    if (favorites.contains(activityToDisplay.activityId)) {
      TopBarLogo(Icons.Filled.Favorite, { flipFavorite(activityToDisplay.activityId) }, isOnline)
    } else {
      TopBarLogo(
          Icons.Filled.FavoriteBorder, { flipFavorite(activityToDisplay.activityId) }, isOnline)
    }
  }
}

// Logo of the top bar
@Composable
fun TopBarLogo(
    logoPainterId: Int,
    isFriendProf: Boolean = false,
    f: () -> Unit,
    isOnline: ConnectionState
) {
  IconButton(
      modifier = Modifier.testTag("TopBarLogo"),
      onClick = { f() },
      enabled = isOnline == ConnectionState.CONNECTED) {
        Icon(
            painter = painterResource(id = logoPainterId),
            contentDescription = "Top Bar logo $logoPainterId",
            modifier = Modifier.width(26.dp).height(26.dp),
            // put to white if bool else put to default color
            tint =
                if (isFriendProf)
                    if (isOnline == ConnectionState.CONNECTED) Color.White
                    else MaterialTheme.colorScheme.inversePrimary
                else if (isOnline == ConnectionState.CONNECTED) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.inversePrimary)
      }
}

// Logo of the top bar for vector images
@Composable
fun TopBarLogo(logoPainterId: ImageVector, f: () -> Unit, isOnline: ConnectionState) {
  IconButton(modifier = Modifier.testTag("TopBarLogo"), onClick = { f() }) {
    Icon(
        imageVector = logoPainterId,
        contentDescription = "Top Bar logo fav ${logoPainterId.name}",
        modifier = Modifier.width(26.dp).height(26.dp),
        tint =
            if (isOnline == ConnectionState.CONNECTED) {
              MaterialTheme.colorScheme.onSurface
            } else {
              MaterialTheme.colorScheme.inversePrimary
            })
  }
}

// Displays the title of the activity, its type and its duration
@Composable
fun ActivityTitleZone(
    activityToDisplay: Activity,
    updateDifficulty: (String) -> Unit,
    isOnline: ConnectionState,
    centerPoint: LatLng
) {
  Row(
      modifier =
          Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 0.dp, bottom = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween) {
        Row(
            modifier = Modifier.padding(8.dp).weight(0.65f),
            verticalAlignment = Alignment.CenterVertically) {
              ActivityPicture(activityToDisplay)
              Spacer(modifier = Modifier.width(8.dp))
              ActivityTitleText(activityToDisplay, centerPoint)
            }
        ElevatedDifficultyDisplay(
            activityToDisplay, updateDifficulty = updateDifficulty, isOnline = isOnline)
      }
}

// Displays the picture of the activity
@Composable
fun ActivityPicture(activityToDisplay: Activity) {
  val defaultId =
      when (activityToDisplay.activityType) {
        ActivityType.HIKING -> R.drawable.hiking_roundicon
        ActivityType.CLIMBING -> R.drawable.climbing_roundicon
        ActivityType.BIKING -> R.drawable.biking_roundicon
      }
  val testTag =
      when (activityToDisplay.activityType) {
        ActivityType.HIKING -> "HikingPicture"
        ActivityType.CLIMBING -> "ClimbingPicture"
        ActivityType.BIKING -> "BikingPicture"
      }
  Column {
    Image(
        painter = painterResource(id = defaultId),
        contentDescription = "Default Activity Picture",
        modifier = Modifier.size(65.dp).testTag(testTag))
  }
}

@Composable
fun ActivityTitleText(activityToDisplay: Activity, centerPoint: LatLng) {
  Row {
    Column {
      if (activityToDisplay.activityImageUrl != "") {
        ActivityTitleWithURl(activityToDisplay)
      } else {
        ActivityTitleNoURL(activityToDisplay)
      }

      Text(
          text =
              "${String.format("%.1f", SphericalUtil.computeDistanceBetween(centerPoint, LatLng(activityToDisplay.startPosition.lat, activityToDisplay.startPosition.lon)) / 1000)} km away",
          color = AccentGreen,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold)
    }
  }
}

@Composable
fun ActivityTitleNoURL(activityToDisplay: Activity) {
  Text(
      text = activityToDisplay.name,
      color = MaterialTheme.colorScheme.onBackground,
      fontWeight = FontWeight.ExtraBold,
      style = MaterialTheme.typography.headlineLarge)
}

@Composable
fun ActivityTitleWithURl(activityToDisplay: Activity) {
  Text(
      text = activityToDisplay.name,
      color = Color.White,
      fontWeight = FontWeight.ExtraBold,
      style =
          MaterialTheme.typography.headlineLarge.copy(
              shadow = Shadow(color = Color.Black, offset = Offset(x = 1f, y = 2f))))
}

@Composable
fun AddRatingButton(
    activityToDisplay: Activity,
    onShowReviewModal: () -> Unit,
    isReviewing: MutableState<Boolean>,
    text: MutableState<String>,
    writeNewRating: (String, Rating, String) -> Unit,
    currentUser: UserModel?,
    isOnline: ConnectionState
) {
  if (isReviewing.value) {
    ModalRating(isReviewing, activityToDisplay, text, currentUser, writeNewRating, isOnline)
  } else {
    if (isOnline == ConnectionState.CONNECTED) {
      AddRatingButton(onShowReviewModal)
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalRating(
    isReviewing: MutableState<Boolean>,
    activityToDisplay: Activity,
    text: MutableState<String>,
    currentUser: UserModel?,
    writeNewRating: (String, Rating, String) -> Unit,
    isOnline: ConnectionState
) {
  ModalBottomSheet(
      onDismissRequest = { isReviewing.value = false }, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(30.dp).testTag("ModalBottomSheet"),
            horizontalAlignment = Alignment.CenterHorizontally) {
              val selectedStarCount = remember { mutableIntStateOf(1) }
              Text(
                  text =
                      LocalContext.current.getString(R.string.add_review) +
                          " " +
                          activityToDisplay.name,
                  maxLines = 1,
                  style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))

              Spacer(modifier = Modifier.height(30.dp))

              Row(
                  horizontalArrangement = Arrangement.SpaceAround,
                  verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = LocalContext.current.getString(R.string.ask_rating),
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium))
                    StarButtons(selectedStarCount = selectedStarCount, isOnline)
                  }

              OutlinedTextField(
                  value = text.value,
                  label = { Text(text = LocalContext.current.getString(R.string.ask_comment)) },
                  onValueChange = { text.value = it },
                  maxLines = 3,
                  modifier = Modifier.fillMaxWidth())

              // Publish button
              ElevatedButton(
                  onClick = {
                    var newMeanRating =
                        activityToDisplay.ratings.sumOf { it.rating.toInt() } +
                            selectedStarCount.intValue
                    val division = newMeanRating.toDouble() / (activityToDisplay.ratings.size + 1.0)
                    val string = String.format(Locale.US, "%.1f", division)

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
                  modifier = Modifier.fillMaxWidth().padding(top = 10.dp).testTag("PublishButton"),
                  colors =
                      ButtonDefaults.buttonColors(
                          containerColor = PrimaryBlue, contentColor = Color.White)) {
                    Text(
                        text = LocalContext.current.getString(R.string.publish),
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
                  }
            }
      }
}

@Composable
fun AddRatingButton(onShowReviewModal: () -> Unit) {
  IconButton(
      onClick = { onShowReviewModal() },
      colors =
          IconButtonColors(
              containerColor = Color.Yellow,
              contentColor = Color.Black,
              disabledContentColor = Color.Yellow,
              disabledContainerColor = Color.Black),
      modifier = Modifier.size(25.dp).testTag("AddRatingButton")) {
        Icon(
            painter = painterResource(id = R.drawable.plus),
            contentDescription = "Add Rating",
            modifier = Modifier.width(16.dp).height(16.dp),
            tint = Color.Black,
        )
      }
}

@Composable
fun StarButtons(selectedStarCount: MutableState<Int>, isOnline: ConnectionState) {
  Row(
      modifier = Modifier.padding(2.dp).testTag("StarButtons"),
      verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..5) {
          val isSelected = i <= selectedStarCount.value
          StarIconButton(selectedStarCount, i, isSelected, isOnline)
        }
      }
}

@Composable
fun StarIconButton(
    selectedStarCount: MutableState<Int>,
    i: Int,
    isSelected: Boolean,
    isOnline: ConnectionState
) {
  IconButton(
      onClick = { selectedStarCount.value = i },
      modifier = Modifier.size(25.dp),
      enabled = isOnline == ConnectionState.CONNECTED) {
        Icon(
            painter =
                painterResource(
                    id = if (isSelected) R.drawable.filled_star else R.drawable.empty_star),
            contentDescription = "Star $i",
            modifier = Modifier.width(16.dp).height(16.dp),
            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Black)
      }
}

@Composable
fun RatingCard(rating: Rating, usersList: List<UserModel?>) {
  Card(
      modifier =
          Modifier.fillMaxWidth()
              .padding(vertical = 8.dp, horizontal = 16.dp)
              .clickable(onClick = {})
              .testTag("RatingCard"),
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
