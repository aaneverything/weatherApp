package com.aantriav.weatherapp.api

import androidx.core.app.NotificationCompat.MessagingStyle.Message


// T refers to the type of the data that will be returned from the API
sealed class WeatherResponse<out T> {
    data class Success<out T>(val data: T) : WeatherResponse<T>()
    data class Error(val message: String) : WeatherResponse<Nothing>()
    object Loading : WeatherResponse<Nothing>()
}