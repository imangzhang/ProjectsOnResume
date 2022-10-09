package com.example.lifestyle_kalyan

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class WeatherRepository() {

    var weatherData = MutableLiveData<Weather>()

    private var mLatitude: Double? = null
    private var mLongitude: Double? = null
    private var mJsonString: String? = null

    fun setLocation(latitude: Double, longitude: Double) {
        // First cache the location
        mLatitude = latitude
        mLongitude = longitude

        CoroutineScope(Dispatchers.IO).launch {
            fetchAndParseWeatherData(latitude, longitude)

            if(mJsonString!=null) {
                val jsonObj = JSONObject(mJsonString)

                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                    Date(updatedAt*1000)
                )
                val temp = main.getString("temp")+"°F"
                val tempMin = "Min Temp: " + main.getString("temp_min")+"°F"
                val tempMax = "Max Temp: " + main.getString("temp_max")+"°F"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunriseLong:Long = sys.getLong("sunrise")
                val sunrise: String = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunriseLong*1000))
                val sunsetLong:Long = sys.getLong("sunset")
                val sunset: String = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunsetLong*1000))
                val windSpeed = wind.getString("speed") + " mph"
                val weatherDescription = weather.getString("description")

                val address = jsonObj.getString("name")+", "+sys.getString("country")

                val data = Weather(
                    weatherDescription,
                    temp,
                    tempMin,
                    tempMax,
                    pressure,
                    humidity,
                    windSpeed,
                    sunrise,
                    sunset,
                    updatedAtText,
                    address
                )

                weatherData.postValue(data)
            }
        }
    }

    @WorkerThread
    suspend fun fetchAndParseWeatherData(latitude: Double, longitude: Double) {
        val weatherDataURL = NetworkUtil.buildURLFromString(latitude, longitude)
        if(weatherDataURL!=null) {
            val jsonWeatherData = NetworkUtil.getDataFromURL(weatherDataURL)
            if (jsonWeatherData != null) {
                mJsonString = jsonWeatherData
            }
        }
    }
}