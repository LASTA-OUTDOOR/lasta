package com.lastaoutdoor.lasta.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import com.lastaoutdoor.lasta.data.api.weather.getWeatherIconFromId
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue
import java.math.RoundingMode

private const val KELVIN_CONST = 273.15

/**Complete weather report for more info screen. The boolean is used to toggle the wind speed display*/
@Composable
fun WeatherReportBig(weather: WeatherResponse?,displayWind:Boolean) {

    if (weather != null) {
        // the query is answered with a temperature in Kelvin, which we convert to Celsius
        val finalTemp =
            (weather.main.temp - KELVIN_CONST)
                .toBigDecimal()
                .setScale(1, RoundingMode.UP)
                .toDouble()
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = getWeatherIconFromId(weather.weather.firstOrNull()?.icon?:"01d")),
                contentDescription = "Current Weather Logo",
                modifier = Modifier.size(63.dp,63.dp)
            )
            Column(verticalArrangement = Arrangement.Top){
                Text(
                    text = weather.name,
                    fontWeight = FontWeight(1000),
                    fontSize = 14.sp,
                    color=PrimaryBlue
                )
                Text(
                    text = "Humidity: ${weather.main.hum}%",
                    fontWeight = FontWeight(1000),
                    fontSize = 11.sp,
                    color=PrimaryBlue
                )

            }
            Spacer(Modifier.size(40.dp))
            Text(text = "${finalTemp}°C", fontSize = 22.sp,color=PrimaryBlue)
            Spacer(Modifier.size(30.dp))
            if(displayWind) {
                Column {
                    Text(
                        text = "${weather.wind.speed}",
                        color = PrimaryBlue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight(1000)
                    )
                    Text(
                        text = "km/h",
                        color = PrimaryBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight(1000)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.weather_wind),
                    contentDescription = "Wind speed icon",
                    Modifier
                        .width(23.54386.dp)
                        .height(14.dp)
                )
            }
        }
    } else {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Weather report not available ! Allow us to use your location and try again if you want this service ")
            }

        }
    }
}

/**Partial report with only the temperature and a logo for discovery screen. The weather logo is a clickable button*/
@Composable
fun WeatherReportSmall(weather: WeatherResponse?,onIconClick:()->Unit) {
    if(weather!=null){
        Surface{

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    weather.let {
                        val finalTemp =
                            (weather.main.temp - KELVIN_CONST)
                                .toBigDecimal()
                                .setScale(1, RoundingMode.UP)
                                .toDouble()
                        Text(
                            text = "${finalTemp}°C",
                        )
                        IconButton(
                            modifier = Modifier.size(45.dp,45.dp),
                            onClick = onIconClick
                        ){Image(
                            painter = painterResource(
                                id = getWeatherIconFromId(
                                    weather.weather.firstOrNull()?.icon ?: "01d"
                                )
                            ), contentDescription = "Current Weather Logo"
                        )}
                    }
                }

        }
    }
}
