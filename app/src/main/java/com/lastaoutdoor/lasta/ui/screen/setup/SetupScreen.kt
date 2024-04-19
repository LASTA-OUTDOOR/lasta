package com.lastaoutdoor.lasta.ui.screen.setup

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.lastaoutdoor.lasta.data.db.DatabaseManager
import com.lastaoutdoor.lasta.ui.components.DropDownMenuComponent
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
  var expanded by remember { mutableStateOf(false) }

  Column(
      modifier = Modifier.fillMaxSize().padding(horizontal = 30.dp, vertical = 190.dp),
      verticalArrangement = Arrangement.SpaceBetween) {

        // Title "Settings"
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
          Text(
              text = "Settings",
              style = MaterialTheme.typography.headlineLarge,
              color = MaterialTheme.colorScheme.onBackground
          )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
              Text(text = "Select your language: ", style = MaterialTheme.typography.bodyLarge,
                  color = MaterialTheme.colorScheme.onBackground)
              DropDownMenuComponent(
                  items = languages,
                  selectedItem = selectedLanguage,
                  onItemSelected = { selectedLanguage = it },
                  fieldText = "Language")
            }
        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
              Text(
                  text = "Select your favorite outdoor activity: ",
                  style = MaterialTheme.typography.bodyLarge,
                  color = MaterialTheme.colorScheme.onBackground)
            }

        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly) {
              Button(
                  onClick = { isHikingSelected = true },
                  modifier = Modifier.padding(16.dp),
                  colors =
                      if (isHikingSelected) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onBackground)
                      } else {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
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
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                      } else {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimary)
                      }) {
                    Text(text = outdoorActivities[1])
                  }
            }

        Spacer(modifier = Modifier.height(40.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
          Button(
              onClick = {
                val db = DatabaseManager()
                db.updateFieldInUser(
                    authViewModel.user?.userId.toString(), "language", selectedLanguage)
                val prefSport = if (isHikingSelected) "Hiking" else "Climbing"
                db.updateFieldInUser(authViewModel.user?.userId.toString(), "prefSport", prefSport)

                rootNavController.popBackStack()
                rootNavController.navigate(RootScreen.Main.route)
              },
          ) {
            Text(text = "Save")
          }
        }
      }
}
