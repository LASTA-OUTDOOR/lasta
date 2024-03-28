package com.lastaoutdoor.lasta.view.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastaoutdoor.lasta.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun ProfileScreen() {
  var text by remember { mutableStateOf("Add a few words about yourself") }
  Column {
    Row(modifier = Modifier.fillMaxWidth().padding(15.dp)) {
      Image(
          painter = painterResource(id = R.drawable.pov_img),
          contentDescription = null,
          modifier =
              Modifier
                  // Set image size to 80 dp
                  .size(80.dp)
                  // Clip image to be shaped as a circle
                  .clip(CircleShape)
                  // Add a border with a 1.5 dp width and the primary color
                  .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape))

      // Add a horizontal space between the image and the column
      Spacer(modifier = Modifier.width(8.dp))

      Column {
        Text("Scrum Master")

        // Add a vertical space between the author and message texts
        Spacer(modifier = Modifier.height(4.dp))

        Text("Bio")

        // Add a vertical space between the author and message texts
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            modifier = Modifier.border(1.dp, Color.Black, RoundedCornerShape(4.dp)).padding(8.dp),
            singleLine = false,
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            maxLines = 4 // Adjust maxLines as needed
            )
      }
    }

      Row {
          Text("Activity")
      }
    
  }
  // Add a horizontal space between the image and the column
  Spacer(modifier = Modifier.width(8.dp))

  // Recent activities
  LazyVerticalGrid(modifier = Modifier, columns = GridCells.Adaptive(100.dp)) {}
}
