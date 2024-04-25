package com.lastaoutdoor.lasta.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.viewmodel.WeatherViewModel
import java.math.RoundingMode

private const val KELVIN_CONST = 273.15

@Composable
fun WeatherReport(weatherViewModel: WeatherViewModel = hiltViewModel()) {
  val weather = weatherViewModel.weather.observeAsState()
  Surface {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(text = LocalContext.current.getString(R.string.current_weather))
      weather.value?.let { weather ->
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
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  WeatherReport()
}