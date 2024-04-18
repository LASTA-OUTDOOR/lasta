package com.lastaoutdoor.lasta.ui.screen.activities

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.activity.OutdoorActivity
import com.lastaoutdoor.lasta.ui.theme.LastaTheme

@Composable
fun ActivityScreen(activity: OutdoorActivity){
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        item { Spacer(modifier = Modifier.height(15.dp)) }
        item {TopBar()}
        item{ ActivityTitleZone(activity = activity)}

    }
}

@Composable
fun TopBar(){
    Row(modifier = Modifier.fillMaxWidth()) {
        TopBarLogo(R.drawable.arrow_back,{})
        Spacer(modifier = Modifier.width(180.dp))
        TopBarLogo(R.drawable.archive,{})
        TopBarLogo(R.drawable.share,{})
        TopBarLogo(R.drawable.favourite,{})
    }
}

@Composable
fun TopBarLogo(logoPainterId : Int,f:()->Unit){
    IconButton(onClick = { f() }) {
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

@Composable
fun ActivityTitleZone(activity: OutdoorActivity){
    Row { ElevatedActivityType(activity) }
    Row{
        Column{Image(painter = painterResource(id = R.drawable.ellipse), contentDescription = "Soon Activity Picture", modifier = Modifier
            .padding(5.dp)
            .width(70.dp)
            .height(70.dp))}
        Column {
            Text(text=activity.locationName?:"No Title",
                modifier = Modifier.padding(vertical = 25.dp, horizontal = 5.dp),
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF000000)
                ))
        }
    }
}

@Composable
fun ElevatedActivityType(activity: OutdoorActivity){
    ElevatedButton(
        onClick = {},
        contentPadding = PaddingValues(all = 3.dp),
        modifier = Modifier
            .padding(3.dp)
            .height(30.dp)
            .width(70.dp),
        colors =ButtonDefaults.buttonColors(
            containerColor = Color(0, 150, 207, 128)
        )
    ) {
        Text(activity.getActivityType().toString().replaceFirstChar { it.uppercase() }, color = Color.Black, fontSize = 11.sp)
    }
}