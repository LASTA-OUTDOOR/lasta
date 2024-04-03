package com.lastaoutdoor.lasta.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lastaoutdoor.lasta.data.model.UserModel

@Composable
fun ProfileScreen(userModel: UserModel?, onSignOut: () -> Unit) {
  Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        if (userModel?.profilePictureUrl != null) {
          AsyncImage(
              model = userModel.profilePictureUrl,
              contentDescription = "Profile picture",
              modifier = Modifier.size(150.dp).clip(CircleShape),
              contentScale = ContentScale.Crop)
          Spacer(modifier = Modifier.height(16.dp))
        }
        if (userModel?.username != null) {
          Text(
              text = userModel.username,
              color = MaterialTheme.colorScheme.primary,
              textAlign = TextAlign.Center,
              fontSize = 36.sp,
              fontWeight = FontWeight.SemiBold)
          Spacer(modifier = Modifier.height(16.dp))
        }
        Button(onClick = onSignOut) { Text(text = "Sign out") }
      }
}
