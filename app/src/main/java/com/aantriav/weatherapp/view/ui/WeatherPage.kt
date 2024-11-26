package com.aantriav.weatherapp.view.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.aantriav.weatherapp.api.ResponseModel
import com.aantriav.weatherapp.api.WeatherResponse
import com.aantriav.weatherapp.viewModel.WeatherViewModel
import org.jetbrains.annotations.Async

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }

    val weatherResult by viewModel.weatherResult.observeAsState(initial = null)

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city,
                onValueChange = {
                    city = it
                },
                label = { Text("Search any location") }
            )
            IconButton(
                modifier = Modifier
                    .padding(8.dp)
                    .background(color = Color.Gray),
                onClick = {
                    viewModel.getData(city)
                    keyboardController?.hide()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }

        // Penanganan status cuaca
        when (weatherResult) {
            is WeatherResponse.Loading -> {
                CircularProgressIndicator()
            }

            is WeatherResponse.Success -> {
                val data = (weatherResult as WeatherResponse.Success).data
                WeatherDetails(data)
            }

            is WeatherResponse.Error -> {
                val message = (weatherResult as WeatherResponse.Error).message
                Text(message)
            }

            null -> {
                Text("Please enter a city and search.")

            }
        }
    }
}

@Composable
fun WeatherDetails(data: ResponseModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                modifier = Modifier
                    .size(15.dp)
            )
            Text(text = data.location?.name ?: "", fontSize = 20.sp)
        }
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = data.location?.country ?: "", fontSize = 18.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(17.dp))
        Text(
            text = "${data.current?.tempC ?: 0}°C",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
        AsyncImage(
            model = "https:${data.current?.condition?.icon}",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally),
            contentDescription = "Weather Icon",
        )
        Text(
            text = data.current?.condition?.text ?: "",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(17.dp))
        Card(
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // Padding di dalam kartu
                verticalArrangement = Arrangement.spacedBy(8.dp) // Jarak antar item
            ) {
                WeatherKeyVal("Wind", "${data.current?.windKph} km/h")
                WeatherKeyVal("Humidity", "${data.current?.humidity}%")
                WeatherKeyVal("Pressure", "${data.current?.pressureMb} mb")
                WeatherKeyVal("UV Index", "${data.current?.uv}")
                WeatherKeyVal("Visibility", "${data.current?.visKm} km")
                WeatherKeyVal("Cloud", "${data.current?.cloud}%")
            }
        }
    }
}


@Composable
fun WeatherKeyVal(key: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // Padding vertikal untuk Row
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Menyebarkan ruang antara kunci dan nilai
    ) {
        Text(text = key, fontSize = 18.sp, color = Color.Black) // Kunci
        Text(text = value, fontSize = 18.sp, color = Color.Gray) // Nilai
    }
}
