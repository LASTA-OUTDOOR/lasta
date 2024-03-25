package com.lastaoutdoor.lasta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.lastaoutdoor.lasta.resources.C
import com.lastaoutdoor.lasta.ui.theme.LastaTheme

class SecondActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      LastaTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize().semantics { testTag = C.Tag.second_screen_container },
            color = MaterialTheme.colorScheme.background) {
              GreetingRobo("Robolectric")
            }
      }
    }
  }
}

@Composable
fun GreetingRobo(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier.semantics { testTag = C.Tag.greeting_robo })
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
  LastaTheme { GreetingRobo("Robolectric") }
}
