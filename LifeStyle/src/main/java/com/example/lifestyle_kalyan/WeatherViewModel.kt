package com.example.lifestyle_kalyan

import android.app.Application
import androidx.lifecycle.*

class WeatherViewModel(private val repository : WeatherRepository) : ViewModel() {
    private val weatherData: LiveData<Weather> = repository.weatherData

    private var mLocation: String? = null

    fun setLocation(latitude: Double, longitude: Double) {
        repository.setLocation(latitude, longitude)
    }

    val data: LiveData<Weather>
        get() = weatherData

}

class WeatherViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) : T {
        if(modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}