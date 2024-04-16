package com.lastaoutdoor.lasta.ui.screen.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.activity.OutdoorActivity
import com.lastaoutdoor.lasta.ui.theme.LastaTheme

@Preview
@Composable
fun ActivityScreen(){
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {TopBar()}
    }
}

@Composable
fun TopBar(){
    Row(modifier = Modifier.fillMaxWidth()) {
        Column {BackArrowButton()
        }

    }
}

@Composable
fun BackArrowButton(){
    IconButton(onClick = { /* go back */ }) {
        Icon(
            painter = painterResource(id = R.drawable.arrow_back),
            contentDescription = "Back Arrow",
            modifier = Modifier.padding(1.dp).width(26.dp) .height(26.dp),
            tint =  Color(0, 150, 207, 255)
        )
    }
}