package com.lastaoutdoor.lasta.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.api.weather.WeatherForecast
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import com.lastaoutdoor.lasta.data.api.weather.getWeatherIconFromId
import com.lastaoutdoor.lasta.data.api.weather.kelvinToCelsius
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue

/**
 * Complete weather report for more info screen. The boolean is used to toggle the wind speed
 * display
 */
@Composable
fun WeatherReportBig(weather: WeatherResponse?, displayWind: Boolean, onClick: () -> Unit) {

  if (weather != null) {
    // the query is answered with a temperature in Kelvin, which we convert to Celsius
    val finalTemp = kelvinToCelsius((weather.main.temp))
    Row(
        modifier =
            Modifier.fillMaxWidth()
                .padding(8.dp)
                .clickable { onClick() }
                .testTag("WeatherReportBig"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter =
                    painterResource(
                        id = getWeatherIconFromId(weather.weather.firstOrNull()?.icon ?: "01d")),
                contentDescription = "Current Weather Logo",
                modifier = Modifier.size(63.dp).testTag("WeatherIcon"))

            Column(
                verticalArrangement = Arrangement.Top, modifier = Modifier.testTag("WeatherName")) {
                  Text(text = weather.name, fontWeight = FontWeight(1000), fontSize = 14.sp)
                  Text(
                      text =
                          "${LocalContext.current.getString(R.string.humidity)}: ${weather.main.hum}%",
                      fontWeight = FontWeight(1000),
                      fontSize = 11.sp,
                      color = PrimaryBlue)
                }
          }
          Text(
              text = "${finalTemp}°C",
              fontSize = 22.sp,
              color = PrimaryBlue,
              modifier = Modifier.testTag("temp"))

          if (displayWind) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Image(
                  painter = painterResource(id = R.drawable.weather_wind),
                  contentDescription = "Wind speed icon",
                  Modifier.size(50.dp))

              Column(modifier = Modifier.testTag("WindDisplay")) {
                Text(
                    text = "${weather.wind.speed}",
                    fontSize = 14.sp,
                    color = PrimaryBlue,
                    fontWeight = FontWeight(1000))
                Text(
                    text = "km/h",
                    fontSize = 11.sp,
                    color = PrimaryBlue,
                    fontWeight = FontWeight(1000))
              }

              Spacer(modifier = Modifier.width(8.dp))
            }
          }
        }
  } else {
    Surface {
      Column(modifier = Modifier.padding(16.dp).testTag("NoLocMessage")) {
        Text(text = LocalContext.current.getString(R.string.no_loc))
      }
    }
  }
}

/**
 * Partial report with only the temperature and a logo for discovery screen. The weather logo is a
 * clickable button
 */
@Composable
fun WeatherReportSmall(weather: WeatherResponse?, onIconClick: () -> Unit) {
  if (weather != null) {
    Surface {
      Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center) {
            weather.let {
              val finalTemp = kelvinToCelsius(weather.main.temp)
              Text(text = "${finalTemp}°C", modifier = Modifier.testTag("temp"))
              IconButton(modifier = Modifier.size(45.dp), onClick = onIconClick) {
                Image(
                    painter =
                        painterResource(
                            id =
                                getWeatherIconFromId(weather.weather.firstOrNull()?.icon ?: "01d")),
                    contentDescription = "Current Weather Logo")
              }
            }
          }
    }
  }
}

@Composable
fun WeatherForecastDisplay(weatherForecast: WeatherForecast?, date: String) {
  if (weatherForecast != null) {
    val finalTemp = kelvinToCelsius(weatherForecast.main.temp)

    // display the forecast
    Card {
      Column(
          modifier = Modifier.padding(8.dp).fillMaxWidth().testTag("forecast"),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text =
                    "${LocalContext.current.getString(R.string.forecasted_weather)} ${LocalContext.current.getString(R.string.at_hour)} $date",
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                  Image(
                      painter =
                          painterResource(
                              id =
                                  getWeatherIconFromId(
                                      weatherForecast.weather.firstOrNull()?.icon ?: "01d")),
                      contentDescription = "Weather Icon",
                      modifier = Modifier.size(50.dp).testTag("forecastIcon"))

                  Text(text = "$finalTemp°C", color = PrimaryBlue)

                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                      text =
                          "${LocalContext.current.getString(R.string.humidity)} ${weatherForecast.main.hum}%",
                      color = PrimaryBlue)
                }
          }
    }
  }
}
