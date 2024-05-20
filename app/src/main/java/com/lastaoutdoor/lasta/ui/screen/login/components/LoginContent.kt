package com.lastaoutdoor.lasta.ui.screen.login.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastaoutdoor.lasta.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginContent(onLoginClick: () -> Unit) {
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
                ))

        LoginPager()

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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginPager() {

  val pagerState = rememberPagerState(pageCount = { 5 })

  Column(modifier = Modifier.fillMaxHeight(0.65f), verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {

      // The pager composable is a horizontal pager that allows users to swipe through different pages of content
      HorizontalPager(
          state = pagerState, modifier = Modifier
              .testTag("HorizontalPager")
              .fillMaxHeight(0.9f)

      ) { page ->

          Column(
              modifier = Modifier
                  .fillMaxWidth()
                  .fillMaxHeight(),
              horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly
          ) {
              PagerContent(page = page)
          }
      }
      HorizontalPagerIndicator(
          pageCount = pagerState.pageCount,
          currentPage = pagerState.currentPage,
          targetPage = pagerState.targetPage,
          currentPageOffsetFraction = pagerState.currentPageOffsetFraction
      )
  }

}

//Got this from internet, need to be changed, it is more of a preview
@Composable
private fun HorizontalPagerIndicator(
    pageCount: Int,
    currentPage: Int,
    targetPage: Int,
    currentPageOffsetFraction: Float,
    modifier: Modifier = Modifier,
    indicatorColor: Color = Color.DarkGray,
    unselectedIndicatorSize: Dp = 12.dp,
    selectedIndicatorSize: Dp = 14.dp,
    indicatorCornerRadius: Dp = 6.dp,
    indicatorPadding: Dp = 6.dp
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .wrapContentSize()
            .height(selectedIndicatorSize + indicatorPadding * 2)
    ) {

        // draw an indicator for each page
        repeat(pageCount) { page ->
            // calculate color and size of the indicator
            val (color, size) =
                if (currentPage == page || targetPage == page) {
                    // calculate page offset
                    val pageOffset =
                        ((currentPage - page) + currentPageOffsetFraction).absoluteValue
                    // calculate offset percentage between 0.0 and 1.0
                    val offsetPercentage = 1f - pageOffset.coerceIn(0f, 1f)

                    val size =
                        unselectedIndicatorSize + ((selectedIndicatorSize - unselectedIndicatorSize) * offsetPercentage)

                    indicatorColor.copy(
                        alpha = offsetPercentage
                    ) to size
                } else {
                    indicatorColor.copy(alpha = 0.1f) to unselectedIndicatorSize
                }

            // draw indicator
            Box(
                modifier = Modifier
                    .padding(
                        // apply horizontal padding, so that each indicator is same width
                        horizontal = ((selectedIndicatorSize + indicatorPadding * 2) - size) / 2,
                        vertical = size / 4
                    )
                    .clip(RoundedCornerShape(indicatorCornerRadius))
                    .background(color)
                    .width(size)
                    .height(size / 2)
            )
        }
    }
}

@Composable
fun PagerContent(page : Int){
    when(page){
        0 -> {
            //Fist page : LOGO + ask to swipe
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .testTag("loginLogo"),
                contentScale = ContentScale.Crop
            )
            Text(text = "Swipe to learn more...")
        }
        1 -> {
            Text(text = "Page 2")
        }
        2 -> {
            Text(text = "Page 3")
        }
        3 -> {
            Text(text = "Page 4")
        }
        4 -> {
            Text(text = "Page 5")
        }
    }
}
