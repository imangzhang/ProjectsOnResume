package com.example.lifestyle_kalyan
import org.junit.Test

import org.junit.Assert.*

class BMRCalcTest {
    var bmrCalcHelper = BMRCalcHelper()

    @Test
    fun testBMRCalculationIsCorrect() {
        assertEquals(1689, bmrCalcHelper.calculateBMR(70, 150, 30, "Male"))
        assertEquals(1494, bmrCalcHelper.calculateBMR(70, 150, 30, "Female"))
    }

    @Test
    fun testBMRCalculationWithActivityLevelIsCorrect() {
        val bmrMen = bmrCalcHelper.calculateBMR(70, 150, 30, "Male")
        val bmrWomen = bmrCalcHelper.calculateBMR(70, 150, 30, "Female")

        // Women
        assertEquals(1792, bmrCalcHelper.calculateBMRWithActivityLevel(bmrWomen, "Sedentary"))
        assertEquals(1940, bmrCalcHelper.calculateBMRWithActivityLevel(bmrWomen, "Mild"))
        assertEquals(2239, bmrCalcHelper.calculateBMRWithActivityLevel(bmrWomen, "Moderate"))
        assertEquals(2539, bmrCalcHelper.calculateBMRWithActivityLevel(bmrWomen, "Heavy"))
        assertEquals(2838, bmrCalcHelper.calculateBMRWithActivityLevel(bmrWomen, "Extreme"))

        // Men
        assertEquals(2026, bmrCalcHelper.calculateBMRWithActivityLevel(bmrMen, "Sedentary"))
        assertEquals(2194, bmrCalcHelper.calculateBMRWithActivityLevel(bmrMen, "Mild"))
        assertEquals(2531, bmrCalcHelper.calculateBMRWithActivityLevel(bmrMen, "Moderate"))
        assertEquals(2871, bmrCalcHelper.calculateBMRWithActivityLevel(bmrMen, "Heavy"))
        assertEquals(3209, bmrCalcHelper.calculateBMRWithActivityLevel(bmrMen, "Extreme"))
    }

    @Test
    fun testBMRCalculationIsIncorrect(){
        assertNotEquals(0, bmrCalcHelper.calculateBMR(70, 150, 30, "Male"))
        assertNotEquals(0, bmrCalcHelper.calculateBMR(70, 150, 30, "Female"))
    }

    @Test
    fun testBMRCalculationWithActivityLevelIsIncorrect() {
        val bmrMen = bmrCalcHelper.calculateBMR(70, 150, 30, "Male")
        val bmrWomen = bmrCalcHelper.calculateBMR(70, 150, 30, "Female")

        // Women
        assertNotEquals(0, bmrCalcHelper.calculateBMRWithActivityLevel(bmrWomen, "Sedentary"))
        assertNotEquals(0, bmrCalcHelper.calculateBMRWithActivityLevel(bmrWomen, "Mild"))
        assertNotEquals(0, bmrCalcHelper.calculateBMRWithActivityLevel(bmrWomen, "Moderate"))
        assertNotEquals(0, bmrCalcHelper.calculateBMRWithActivityLevel(bmrWomen, "Heavy"))
        assertNotEquals(0, bmrCalcHelper.calculateBMRWithActivityLevel(bmrWomen, "Extreme"))

        // Men
        assertNotEquals(0, bmrCalcHelper.calculateBMRWithActivityLevel(bmrMen, "Sedentary"))
        assertNotEquals(0, bmrCalcHelper.calculateBMRWithActivityLevel(bmrMen, "Mild"))
        assertNotEquals(0, bmrCalcHelper.calculateBMRWithActivityLevel(bmrMen, "Moderate"))
        assertNotEquals(0, bmrCalcHelper.calculateBMRWithActivityLevel(bmrMen, "Heavy"))
        assertNotEquals(0, bmrCalcHelper.calculateBMRWithActivityLevel(bmrMen, "Extreme"))
    }
}