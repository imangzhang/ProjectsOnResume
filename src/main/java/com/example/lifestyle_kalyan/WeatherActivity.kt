package com.example.lifestyle_kalyan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import com.example.lifestyle_kalyan.databinding.ActivityWeatherBinding



class WeatherActivity : AppCompatActivity() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    val API: String = "5ddddc31e4a00da52af890c8231ec978"
    private lateinit var binding:ActivityWeatherBinding

    private val mWeatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory((application as UserApplication).weatherRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)

        // Loaders
        findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
        findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
        findViewById<TextView>(R.id.errorText).visibility = View.GONE

        // Set location
        mWeatherViewModel.setLocation(latitude, longitude)
        mWeatherViewModel.data.observe(this, weatherDataObserver)

    }

    private val weatherDataObserver: androidx.lifecycle.Observer<Weather> =
        androidx.lifecycle.Observer { weatherData ->
            if(weatherData != null) {

                /* Views populated, Hiding the loader, Showing the main design */
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

                /* Populating extracted data into our views */
                binding.address.text = weatherData.address
                findViewById<TextView>(R.id.updated_at).text =  weatherData.updatedAtText
                findViewById<TextView>(R.id.status).text = weatherData.weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = weatherData.temp
                findViewById<TextView>(R.id.temp_min).text = weatherData.tempMin
                findViewById<TextView>(R.id.temp_max).text = weatherData.tempMax
                findViewById<TextView>(R.id.sunrise).text = weatherData.sunrise
                findViewById<TextView>(R.id.sunset).text = weatherData.sunset
                findViewById<TextView>(R.id.wind).text = weatherData.windSpeed

            }
            else {
            }
        }
}
