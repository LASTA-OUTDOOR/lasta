package com.lastaoutdoor.lasta.ui.screen.login.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.utils.ConnectionState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginContent(onLoginClick: () -> Unit, isConnected : ConnectionState) {
  Column(
      modifier = Modifier
          .fillMaxSize()
          .padding(15.dp)
          .testTag("loginScreen"),
      verticalArrangement = Arrangement.SpaceAround,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = LocalContext.current.getString(R.string.app_name_uppercase),
            modifier = Modifier
                .width(256.dp)
                .height(65.dp)
                .testTag("appName"),
            style =
                TextStyle(
                    fontSize = 57.sp,
                    lineHeight = 64.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    fontFamily = MaterialTheme.typography.titleLarge.fontFamily))

        LoginPager()

        if(isConnected == ConnectionState.CONNECTED){
            OnlineButton(onLoginClick)
        }else{
            OfflineText()
        }
      }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginPager() {

  val pagerState = rememberPagerState(pageCount = { 4 })

  Column(
      modifier = Modifier.fillMaxHeight(0.65f),
      verticalArrangement = Arrangement.SpaceEvenly,
      horizontalAlignment = Alignment.CenterHorizontally) {

        // The pager composable is a horizontal pager that allows users to swipe through different
        // pages of content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .testTag("HorizontalPager")
                .fillMaxHeight(0.9f)) { page ->
              Column(
                  modifier = Modifier
                      .fillMaxWidth()
                      .fillMaxHeight(),
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.SpaceEvenly) {
                    PagerContent(page = page)
                  }
            }
        HorizontalPagerIndicator(
            pageCount = pagerState.pageCount,
            currentPage = pagerState.currentPage,
        )
      }
}

@Composable
private fun HorizontalPagerIndicator(
    pageCount: Int,
    currentPage: Int,
) {
  Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
          .wrapContentSize()
          .height(30.dp)
          .testTag("pageIndicator")) {
        val indicatorColor = MaterialTheme.colorScheme.primary

        repeat(pageCount) { page ->
          val color =
              if (page == currentPage) {
                indicatorColor
              } else {
                indicatorColor.copy(alpha = 0.2f)
              }
          Canvas(
              modifier =
                  Modifier.padding(
                      // apply horizontal padding, so that each indicator is same width
                      horizontal = 14.dp,
                      vertical = 30.dp),
              onDraw = { drawCircle(color = color, radius = 6.dp.toPx()) })
        }
      }
}

// Content that will be displayed in the pager pages
@Composable
fun PagerContent(page: Int) {
  when (page) {
    0 -> {
      FirstPage()
    }
    1 -> {
      SecondPage()
    }
    2 -> {
      ThirdPage()
    }
    3 -> {
      FourthPage()
    }
  }
}

@Composable
fun FirstPage() {
  // Fist page : LOGO + ask to swipe
  Image(
      painter = painterResource(id = R.drawable.app_logo),
      contentDescription = "App Logo",
      modifier = Modifier
          .size(240.dp)
          .clip(CircleShape)
          .testTag("loginLogo"),
      contentScale = ContentScale.Crop)
  Text(
      text = LocalContext.current.getString(R.string.login_swipe_text),
      Modifier.testTag("swipeText"),
      textAlign = TextAlign.Center)
}

@Composable
fun SecondPage() {
  // Second page : Short description and explain activities
  Text(
      text = LocalContext.current.getString(R.string.login_activities_text),
      Modifier
          .padding(10.dp)
          .testTag("activityText"),
      textAlign = TextAlign.Center)

  Image(
      painter = painterResource(id = R.drawable.activities_example),
      contentDescription = "App Logo",
      modifier = Modifier
          .size(350.dp)
          .testTag("activitiesExample"),
      contentScale = ContentScale.Fit)
}

@Composable
fun ThirdPage() {
  // Third page : Short description and explain social
  // android's contact icon
  Icon(
      Icons.Filled.AccountBox,
      contentDescription = "Community Icon",
      modifier = Modifier
          .fillMaxSize(0.6f)
          .testTag("CommunityIcon"),
      tint = MaterialTheme.colorScheme.onBackground)
  Text(
      text = LocalContext.current.getString(R.string.login_friends_text),
      Modifier
          .padding(10.dp)
          .testTag("CommunityText"),
      textAlign = TextAlign.Center)
}

@Composable
fun FourthPage() {
  // Fourth page : It's free, try it now !
  Image(
      painter = painterResource(id = R.drawable.handshake),
      contentDescription = "App Logo",
      modifier = Modifier
          .size(250.dp)
          .testTag("handshakeImage"),
      contentScale = ContentScale.Fit,
      // we want it to be colored with the theme
      colorFilter =
          androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.onBackground))
  Text(
      text = LocalContext.current.getString(R.string.login_free_text),
      Modifier
          .padding(10.dp)
          .testTag("JoinText"),
      textAlign = TextAlign.Center)
}
@Composable
fun OnlineButton(onLoginClick: () -> Unit){
    Button(
        onClick = { onLoginClick() },
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.Gray),
        modifier = Modifier.testTag("loginButton"),
        content = {
            Image(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp))
            Text(
                text = LocalContext.current.getString(R.string.sign_in_google),
                modifier = Modifier.padding(6.dp),
                color = Color.Black,
                fontSize = 14.sp)
        })
}
@Composable
fun OfflineText(){
    Text(LocalContext.current.getString(R.string.offline_text), textAlign = TextAlign.Center)
}


