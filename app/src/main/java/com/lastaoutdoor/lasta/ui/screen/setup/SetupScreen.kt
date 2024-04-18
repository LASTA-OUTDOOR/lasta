package com.lastaoutdoor.lasta.ui.screen.setup

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.navigation.RootScreen
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel

@SuppressLint("MutableCollectionMutableState")
@Composable
fun SetupScreen(
    rootNavController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {

  Modifier.testTag("SetupScreen")

  val languages = listOf("English", "French", "Spanish")

  var selectedLanguage by remember { mutableStateOf(languages[0]) }
  val outdoorActivities = listOf("Hiking", "Climbing")
  var isHikingSelected by remember { mutableStateOf(true) }

  Scaffold(topBar = { TopAppBar(title = { Text(stringResource(R.string.setup_title)) }) }) {
      paddingValues ->
    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
    ) {
      // Language Selection
      Text(text = "Please select your language", style = MaterialTheme.typography.bodyLarge)
      DropdownMenu(
          expanded = false, // Initially closed
          onDismissRequest = {},
          modifier = Modifier.fillMaxWidth()) {
            languages.forEach { language ->
              DropdownMenuItem(onClick = { selectedLanguage = language }) { Text(text = language) }
            }
          }
      Spacer(modifier = Modifier.height(16.dp))

      // Favorite Activities
      Text(
          text = "Please select your favorite outdoor activities",
          style = MaterialTheme.typography.bodyLarge)

      Button(
          onClick = { isHikingSelected = true },
          modifier = Modifier.padding(16.dp),
          colors =
              if (isHikingSelected) {
                ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary)
              } else {
                ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary)
              }) {
            Text(text = outdoorActivities[0])
          }
      Button(
          onClick = { isHikingSelected = false },
          modifier = Modifier.padding(16.dp),
          colors =
              if (!isHikingSelected) {
                ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary)
              } else {
                ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary)
              }) {
            Text(text = outdoorActivities[1])
          }

      Spacer(modifier = Modifier.height(16.dp))

      // Button (Functionality not included)
      Button(
          onClick = {
            // TODO Save
            rootNavController.popBackStack()
            rootNavController.navigate(RootScreen.Main.route)
          },
          modifier = Modifier.fillMaxWidth()) {
            Text(text = "Save")
          }
    }
  }
}
