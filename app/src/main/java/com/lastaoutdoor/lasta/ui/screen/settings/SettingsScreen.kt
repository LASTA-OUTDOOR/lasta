package com.lastaoutdoor.lasta.ui.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.db.DatabaseManager
import com.lastaoutdoor.lasta.ui.components.DropDownMenuComponent
import com.lastaoutdoor.lasta.ui.navigation.RootScreen
import com.lastaoutdoor.lasta.ui.screen.activities.TopBarLogo
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun SettingsScreen(
    navController: NavHostController,
    preferencesViewModel: PreferencesViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var selectedLanguage by remember { mutableStateOf(preferencesViewModel.language) }
    val database = DatabaseManager()

    //If prefViewmodel pref sport is hiking set a called "isHiking" to true
    val isHiking = preferencesViewModel.prefSport.collectAsState(initial = "").value == "Hiking"

    var isHikingSelected by remember { mutableStateOf(isHiking) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp, vertical = 190.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        TopBarLogo(logoPainterId = R.drawable.arrow_back) {
            navController.navigateUp()
        }

        // Title "Settings"
        Text(
            text = "Account settings",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Language selection
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Select your language: ",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            DropDownMenuComponent(
                items = listOf("English", "French", "German"),
                selectedItem = selectedLanguage,
                onItemSelected = { selectedLanguage = it as Flow<String>
                                    preferencesViewModel.updateLanguage(it)
                                    database.updateFieldInUser(authViewModel.user!!.userId, "language", it)
                                 },
                fieldText = "Language"
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Outdoor activity selection
        Text(
            text = "Select your favorite outdoor activity: ",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { isHikingSelected = true
                          preferencesViewModel.updatePrefSport("Hiking")
                          database.updateFieldInUser(authViewModel.user!!.userId, "prefSport", "Hiking")},
                modifier = Modifier.padding(16.dp),
                colors =
                if (isHikingSelected) {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                }
            ) {
                Text(text = "Hiking")
            }
            Button(
                onClick = { isHikingSelected = false
                          preferencesViewModel.updatePrefSport("Climbing")
                          database.updateFieldInUser(authViewModel.user!!.userId, "prefSport", "Climbing")},
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
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                }
            ) {
                Text(text = "Climbing")
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(
                onClick = {

                    // Sign out the user
                    authViewModel.signOut()

                    // Navigate to the Login screen
                    navController.navigate(RootScreen.Login.route)
                }
            ) {
                Text(text = "Sign Out")
            }
        }
    }
}
