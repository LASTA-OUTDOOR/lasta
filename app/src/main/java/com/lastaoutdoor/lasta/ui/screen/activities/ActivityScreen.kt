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
import androidx.compose.ui.graphics.painter.Painter
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
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        item { Spacer(modifier = Modifier.height(15.dp)) }
        item {TopBar()}
    }
}

@Composable
fun TopBar(){
    Row(modifier = Modifier.fillMaxWidth()) {
        TopBarLogo(R.drawable.arrow_back)
        Spacer(modifier = Modifier.width(180.dp))
        TopBarLogo(R.drawable.archive)
        TopBarLogo(R.drawable.share)
        TopBarLogo(R.drawable.favourite)
    }
}

@Composable
fun TopBarLogo(logoPainterId : Int){
    IconButton(onClick = { /* go back */ }) {
        Icon(
            painter = painterResource(id =logoPainterId),
            contentDescription = "Top Bar logo",
            modifier = Modifier
                .width(26.dp)
                .height(26.dp),
            tint =  Color(0, 150, 207, 255)
        )
    }
}