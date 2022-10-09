package com.example.lifestyle_kalyan

import kotlin.math.roundToInt

class BMRCalcHelper {
    fun calculateBMR(height: Int?, weight: Int?, age: Int?, gender: String?): Int {
        val weightConverted = weight?.times(0.453592)
        val heightConverted = height?.times(2.54)

        if(gender == "Male") {
            return (66.47 + (13.75 * weightConverted!!) + (5.003 * heightConverted!!) - (6.755 * age!!)).roundToInt()
        }
        else if(gender == "Female") {
            return (655.1 + (9.563 * weightConverted!!) + (1.850 * heightConverted!!) - (4.676 * age!!)).roundToInt()
        }

        return 0
    }

    fun calculateBMRWithActivityLevel(BMR: Int, activityLevel: String?): Int {
        if(activityLevel == "Sedentary") {
            return (BMR * 1.2).toInt()
        }
        else if(activityLevel == "Mild") {
            return ((BMR * 1.3) - 1.375).toInt()
        }
        else if(activityLevel == "Moderate") {
            return ((BMR * 1.5) - 1.55).toInt()
        }
        else if(activityLevel == "Heavy") {
            return (BMR * 1.7).toInt()
        }
        else if(activityLevel == "Extreme") {
            return (BMR * 1.9).toInt()
        }

        return 0
    }
}