package com.lastaoutdoor.lasta.ui.screen.setup

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lastaoutdoor.lasta.R


@SuppressLint("MutableCollectionMutableState")
@Composable
fun SetupScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    Modifier.testTag("SetupScreen")

    val languages = listOf(
        "English",
        "French",
        "Spanish"
    )

    var selectedLanguage by remember { mutableStateOf(languages[0]) }
    val outdoorActivities = listOf(
        "Hiking",
        "Climbing"
    )
    var selectedActivities by remember { mutableStateOf(mutableListOf<String>())}

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.setup_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            // Language Selection
            Text(
                text = "Please select your language",
                style = MaterialTheme.typography.h6
            )
            DropdownMenu(
                expanded = false, // Initially closed
                onDismissRequest = {
                                   // TODO Store selected language in user db
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                languages.forEach { language ->
                    DropdownMenuItem(onClick = { selectedLanguage = language }) {
                        Text(text = language)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Favorite Activities
            Text(
                text = stringResource(R.string.setup_activities),
                style = MaterialTheme.typography.h6
            )
            CheckboxGroup(
                selected = selectedActivities,
                onSelectedChange = { selectedActivities = it.toMutableList() }
            ) { activity ->
                Checkbox(
                    checked = selectedActivities.contains(activity),
                    onCheckedChange = { isChecked ->
                        if (isChecked) selectedActivities.add(activity)
                        else selectedActivities.remove(activity)
                    },
                    label = { Text(text = activity) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Button (Functionality not included)
            Button(
                onClick = { /* Handle button click and potentially save settings */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save")
            }
        }
    }


}