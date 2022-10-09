package com.example.lifestyle_kalyan

import androidx.annotation.WorkerThread
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

object NetworkUtil {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/weather?"
    private const val LATQUERY = "lat="
    private const val LONQUERY = "&lon="
    private const val APPIDQUERY = "&appid="
    private const val API_KEY = "5ddddc31e4a00da52af890c8231ec978"
    private const val UNITSQUERY = "&units=imperial"

    fun buildURLFromString(latitude: Double, longitude: Double): URL? {
        var myURL: URL? = null
        try {
            myURL = URL(BASE_URL + LATQUERY + latitude + LONQUERY + longitude + UNITSQUERY + APPIDQUERY + API_KEY)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return myURL
    }

    @WorkerThread
    fun getDataFromURL(url: URL): String? {
        val urlConnection = url.openConnection() as HttpURLConnection
        return try {
            val inputStream = urlConnection.inputStream

            //The scanner trick: search for the next "beginning" of the input stream
            //No need to user BufferedReader
            val scanner = Scanner(inputStream)
            scanner.useDelimiter("\\A")
            val hasInput = scanner.hasNext()
            if (hasInput) {
                scanner.next()
            } else {
                null
            }
        } finally {
            urlConnection.disconnect()
        }
    }
}
