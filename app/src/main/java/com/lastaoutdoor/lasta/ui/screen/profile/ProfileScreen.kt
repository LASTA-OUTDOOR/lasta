package com.lastaoutdoor.lasta.ui.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.lastaoutdoor.lasta.data.model.user.HikingLevel
import com.lastaoutdoor.lasta.ui.navigation.RootScreen
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel

@Composable
fun ProfileScreen(
    rootNavController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
  // val isLoggedIn by preferencesViewModel.isLoggedIn.collectAsState(initial = false)
  // val userId by preferencesViewModel.userId.collectAsState(initial = "")
  val userName by preferencesViewModel.userName.collectAsState(initial = "")
  // val email by preferencesViewModel.email.collectAsState(initial = "")
  val profilePictureUrl by preferencesViewModel.profilePictureUrl.collectAsState(initial = "")
  val hikingLevel by preferencesViewModel.hikingLevel.collectAsState(initial = HikingLevel.BEGINNER)
  Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = profilePictureUrl,
            contentDescription = "Profile picture",
            modifier = Modifier.size(150.dp).clip(CircleShape),
            contentScale = ContentScale.Crop)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userName,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
              authViewModel.signOut()
              preferencesViewModel.clearPreferences()
              preferencesViewModel.updateIsLoggedIn(false)
              rootNavController.popBackStack()
              rootNavController.navigate(RootScreen.Login.route)
            }) {
              Text(text = "Sign out")
            }
        Spacer(modifier = Modifier.height(16.dp))
        HikingRow(preferences = preferencesViewModel, selectedHikingLevel = hikingLevel)
      }
}

@Composable
fun HikingRow(preferences: PreferencesViewModel, selectedHikingLevel: HikingLevel) {
  Row(
      modifier = Modifier.fillMaxWidth(.7f),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically) {
        HikingLevel.values().forEach { hikingLevel ->
          Text(text = hikingLevel.level.toString(), maxLines = 1, overflow = TextOverflow.Ellipsis)
          RadioButton(
              selected = hikingLevel == selectedHikingLevel,
              onClick = { preferences.updateHikingLevel(hikingLevel) })
        }
      }
}
