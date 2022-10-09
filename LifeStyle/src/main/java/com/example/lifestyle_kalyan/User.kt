package com.example.lifestyle_kalyan

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_v2")
data class User(
    @PrimaryKey val id: Int,
    val name: String,
    val age: Int?,
    val gender: String,
    val city: String,
    val country: String,
    val weight: Int?,
    val height: Int?,
    val activityLevel: String,
    val profilePictureLink: String,
    val latitude: Double,
    val longitude: Double
)
