package com.example.lifestyle_kalyan

data class UserInfoModel(
    val id: Int,
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