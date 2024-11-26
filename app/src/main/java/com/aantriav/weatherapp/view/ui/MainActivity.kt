package com.aantriav.weatherapp.view.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModelProvider
import com.aantriav.weatherapp.view.ui.theme.WeatherAppTheme
import com.aantriav.weatherapp.viewModel.WeatherViewModel

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        setContent {
            WeatherAppTheme {
                Surface {
                    WeatherPage(weatherViewModel)
                }
            }
        }
    }
}
