package com.lastaoutdoor.lasta.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.R

/**
 * A search bar component that allows users to search for items.
 *
 * @param modifier Modifier to apply to the search bar.
 * @param onSearch Callback to be invoked when the user searches for an item.
 * @sample SearchBarComponent(modifier = Modifier, onSearch = { searchText -> println(searchText) })
 */
@Composable
fun SearchBarComponent(modifier: Modifier, onSearch: (String) -> Unit) {

  // Focus to hide the keyboard
  val focusManager = LocalFocusManager.current

  var searchText by remember { mutableStateOf(TextFieldValue("")) }
  val iconSize = 48.dp // Adjust icon size as needed
  OutlinedTextField(
      value = searchText,
      onValueChange = {
        searchText = it
        onSearch(it.text)
      },
      placeholder = {
        Text(
            text = LocalContext.current.getString(R.string.search),
            style = MaterialTheme.typography.bodySmall)
      },
      leadingIcon = {
        Icon(
            painter =
                painterResource(
                    id = R.drawable.search_icon), // Replace with your search icon resource
            contentDescription = "Search",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(iconSize) // Adjust icon size as needed
            )
      },
      shape = RoundedCornerShape(20), // Adjust the corner radius to get the desired roundness
      singleLine = true,
      modifier = modifier.height(56.dp), // Match Material Design height for text fields,
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
      keyboardActions = KeyboardActions(
          onDone = {focusManager.clearFocus()}
      )
  )
}
