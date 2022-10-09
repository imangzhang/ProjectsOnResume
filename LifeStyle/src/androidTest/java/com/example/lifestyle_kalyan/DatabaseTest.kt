package com.example.lifestyle_kalyan

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.ArrayList

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private var userInfo = UserInfoModel(
        getRand(), "John Doe", 30, "Male", "Salt Lake City",
            "United States", 160, 70, "Mild", "TEST",
            40.76, -111.11
        )

    val appContext = ApplicationProvider.getApplicationContext<Context>()
    private var databaseHelper = DatabaseHelper(appContext)

    fun getRand(): Int {
        // Get random number
        val randList = ArrayList<Int>()

        for(i in 0..20) {
            randList.add((0..9).random())
        }

        randList.shuffle()

        var randStr = ""

        for(j in 0..8) {
            randStr += randList[j].toString()
        }

        val rand = randStr.toInt()

        return rand
    }

    @Test
    @Throws(Exception::class)
    fun addUserTest() {
        databaseHelper.addUser(userInfo)
        val addedUserInfo = databaseHelper.getUserByID(userInfo.id)

        try {
            if(addedUserInfo != null) {
                assertEquals(addedUserInfo.id, userInfo.id)
                assertEquals(addedUserInfo.name, userInfo.name)
                assertEquals(addedUserInfo.age, userInfo.age)
                assertEquals(addedUserInfo.gender, userInfo.gender)
                assertEquals(addedUserInfo.city, userInfo.city)
                assertEquals(addedUserInfo.country, userInfo.country)
                assertEquals(addedUserInfo.weight, userInfo.weight)
                assertEquals(addedUserInfo.height, userInfo.height)
                assertEquals(addedUserInfo.activityLevel, userInfo.activityLevel)
                assertEquals(addedUserInfo.profilePictureLink, userInfo.profilePictureLink)
                assertEquals(addedUserInfo.latitude, userInfo.latitude, 0.0)
                assertEquals(addedUserInfo.longitude, userInfo.longitude, 0.0)
            }
            else{
                fail("User info is null")
            }
        }
        catch(e: Exception) {
            throw e
        }
    }

}