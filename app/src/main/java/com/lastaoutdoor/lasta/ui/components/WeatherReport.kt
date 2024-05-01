package com.lastaoutdoor.lasta.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import java.math.RoundingMode

private const val KELVIN_CONST = 273.15

/**Complete weather report for more info screen*/
@Composable
fun WeatherReportBig(weather: WeatherResponse?) {

    if (weather != null) {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = LocalContext.current.getString(R.string.current_weather))
                weather.let { weather ->
                    // the query is answered with a temperature in Kelvin, which we convert to Celsius
                    val finalTemp =
                        (weather.main.temp - KELVIN_CONST)
                            .toBigDecimal()
                            .setScale(1, RoundingMode.UP)
                            .toDouble()
                    Text(
                        text = "${LocalContext.current.getString(R.string.city_w)}: ${weather.name}",
                    )
                    Text(
                        text = "${LocalContext.current.getString(R.string.temperature)}: ${finalTemp}°C",
                    )
                    Text(
                        text =
                        "${LocalContext.current.getString(R.string.descr_w)}: ${weather.weather.firstOrNull()?.description ?: "N/A"}",
                    )
                    Text(
                        text =
                        "${LocalContext.current.getString(R.string.wind_speed)}: ${weather.wind.speed} m/s",
                    )
                }
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

/**Partial report with only the temperature and a logo for discovery screen*/
@Composable
fun WeatherReportSmall(weather: WeatherResponse?) {
    if(weather!=null){
        Surface{
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                weather.let {
                    val finalTemp =
                        (weather.main.temp - KELVIN_CONST)
                            .toBigDecimal()
                            .setScale(1, RoundingMode.UP)
                            .toDouble()
                    Text(
                        text = "${finalTemp}°C",
                    )
                    Image(
                        painter = painterResource(id = R.drawable.weather_sun),
                        contentDescription = "Sunny day",
                        modifier = Modifier.size(45.dp,45.dp)
                    )
                }
            }
        }
    }
}
