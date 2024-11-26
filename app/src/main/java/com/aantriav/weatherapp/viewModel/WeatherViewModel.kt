package com.aantriav.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aantriav.weatherapp.api.ApiConfig
import com.aantriav.weatherapp.api.Constant
import com.aantriav.weatherapp.api.ResponseModel
import com.aantriav.weatherapp.api.WeatherApi
import com.aantriav.weatherapp.api.WeatherResponse
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class WeatherViewModel : ViewModel() {
    private val weatherApi = ApiConfig.weatherApi
    private val _weatherResult = MutableLiveData<WeatherResponse<ResponseModel>>()
    val weatherResult: LiveData<WeatherResponse<ResponseModel>> = _weatherResult


    fun getData(city: String) {
        _weatherResult.value = WeatherResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(Constant.apiKey, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = WeatherResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = WeatherResponse.Error("Failed to get data")
                }
            } catch (
                e: Exception
            ) {
                _weatherResult.value = WeatherResponse.Error(
                    e.message.toString()
                )
            }
        }
    }
}