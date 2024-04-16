package com.lastaoutdoor.lasta.ui.screen.social

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.lastaoutdoor.lasta.ui.theme.LastaTheme

@Composable
fun SocialScreen() {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround){

        Text("Friends", modifier = Modifier.testTag("friends"), fontWeight = FontWeight.Bold, style = TextStyle(fontSize = 24.sp))
        Button(onClick = {  }, modifier = Modifier.testTag("addFriendsButton")) {
            Icon(Icons.Filled.Add, contentDescription = "add friends", tint = MaterialTheme.colorScheme.secondary)
        }
    }
}


//preview
@Preview
@Composable
fun SocialScreenPreview() {
    SocialScreen()
}
