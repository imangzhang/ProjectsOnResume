package com.example.lifestyle_kalyan

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class UserApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy {UserRoomDatabase.getDatabase(this, applicationScope)}
    val repository by lazy {UserRepository(database.userDao())}
    val weatherRepository by lazy {WeatherRepository()}

}