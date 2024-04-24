package com.lastaoutdoor.lasta.ui.screen.login.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

@Composable
fun LoginContent(onLoginClick: () -> Unit) {
  Column(
      modifier = Modifier.fillMaxSize().padding(15.dp).testTag("loginScreen"),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(240.dp).clip(CircleShape).testTag("loginLogo"),
            contentScale = ContentScale.Crop)
        Spacer(modifier = Modifier.size(35.dp))
        Text(
            text = LocalContext.current.getString(R.string.app_name_uppercase),
            modifier = Modifier.width(256.dp).height(65.dp).testTag("appName"),
            style =
                TextStyle(
                    fontSize = 57.sp,
                    lineHeight = 64.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                ))
        Spacer(modifier = Modifier.size(150.dp))
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
