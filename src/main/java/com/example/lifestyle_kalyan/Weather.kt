package com.example.lifestyle_kalyan

data class Weather(
    val weatherDescription: String,
    val temp: String,
    val tempMin: String,
    val tempMax: String,
    val pressure: String,
    val humidity: String,
    val windSpeed: String,
    val sunrise: String,
    val sunset: String,
    val updatedAtText: String,
    val address: String
)
