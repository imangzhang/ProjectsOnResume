package com.example.lifestyle_kalyan

import android.content.Intent
import android.graphics.Color
import android.graphics.Color.WHITE
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.lifestyle_kalyan.databinding.ActivityCalculationBinding
import java.lang.Byte.decode

class CalculationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculationBinding
    private lateinit var bmrCalcHelper: BMRCalcHelper
    private var userID: Int = 0

    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as UserApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userID = intent.getIntExtra("userID", -1)

        // Set user id
        mUserViewModel.setSelectedUser(userID)
        mUserViewModel.userData.observe(this, userDataObserver)

    }

    private val userDataObserver: androidx.lifecycle.Observer<User> =
        androidx.lifecycle.Observer { userInfo ->
            if(userInfo != null) {

                binding.button.setBackgroundColor(Color.WHITE)
                binding.button2.setBackgroundColor(Color.WHITE)
                binding.button3.setBackgroundColor(Color.WHITE)
                binding.button4.setBackgroundColor(Color.WHITE)
                binding.button5.setBackgroundColor(Color.WHITE)

                when (userInfo?.activityLevel) {
                    "Sedentary" -> {
                        binding.button.setBackgroundColor(Color.GRAY)
                    }
                    "Mild" -> {
                        binding.button2.setBackgroundColor(Color.GRAY)
                    }
                    "Moderate" -> {
                        binding.button3.setBackgroundColor(Color.GRAY)
                    }
                    "Heavy" -> {
                        binding.button4.setBackgroundColor(Color.GRAY)
                    }
                    "Extreme" -> {
                        binding.button5.setBackgroundColor(Color.GRAY)
                    }
                }

                bmrCalcHelper = BMRCalcHelper()
                val BMR = bmrCalcHelper.calculateBMR(userInfo?.height, userInfo?.weight, userInfo?.age, userInfo?.gender)
                val BMRWithActivityLevel = bmrCalcHelper.calculateBMRWithActivityLevel(BMR, userInfo?.activityLevel)
                binding.button.text = "Sedentary: ${bmrCalcHelper.calculateBMRWithActivityLevel(BMR, "Sedentary")}"
                binding.button2.text = "Mild: ${bmrCalcHelper.calculateBMRWithActivityLevel(BMR, "Mild")}"
                binding.button3.text = "Moderate: ${bmrCalcHelper.calculateBMRWithActivityLevel(BMR, "Moderate")}"
                binding.button4.text = "Heavy: ${bmrCalcHelper.calculateBMRWithActivityLevel(BMR, "Heavy")}"
                binding.button5.text = "Extreme: ${bmrCalcHelper.calculateBMRWithActivityLevel(BMR, "Extreme")}"


                binding.button.setOnClickListener {
                    if(userInfo != null) {
                        val updatedInfo = User(
                            userInfo.id, userInfo.name, userInfo.age, userInfo.gender,userInfo.city,
                            userInfo.country,userInfo.weight, userInfo.height, "Sedentary",
                            userInfo.profilePictureLink, userInfo.latitude, userInfo.longitude
                        )

                        mUserViewModel.updateUser(updatedInfo)
                    }
                    binding.button.setBackgroundColor(Color.GRAY)
                    binding.button2.setBackgroundColor(Color.WHITE)
                    binding.button3.setBackgroundColor(Color.WHITE)
                    binding.button4.setBackgroundColor(Color.WHITE)
                    binding.button5.setBackgroundColor(Color.WHITE)
                }

                binding.button2.setOnClickListener {
                    if(userInfo != null) {
                        val updatedInfo = User(
                            userInfo.id, userInfo.name, userInfo.age, userInfo.gender,userInfo.city,
                            userInfo.country,userInfo.weight, userInfo.height, "Mild",
                            userInfo.profilePictureLink, userInfo.latitude, userInfo.longitude
                        )

                        mUserViewModel.updateUser(updatedInfo)
                    }
                    binding.button.setBackgroundColor(Color.WHITE)
                    binding.button2.setBackgroundColor(Color.GRAY)
                    binding.button3.setBackgroundColor(Color.WHITE)
                    binding.button4.setBackgroundColor(Color.WHITE)
                    binding.button5.setBackgroundColor(Color.WHITE)
                }

                binding.button3.setOnClickListener {
                    if(userInfo != null) {
                        val updatedInfo = User(
                            userInfo.id, userInfo.name, userInfo.age, userInfo.gender,userInfo.city,
                            userInfo.country,userInfo.weight, userInfo.height, "Moderate",
                            userInfo.profilePictureLink, userInfo.latitude, userInfo.longitude
                        )

                        mUserViewModel.updateUser(updatedInfo)
                    }
                    binding.button.setBackgroundColor(Color.WHITE)
                    binding.button2.setBackgroundColor(Color.WHITE)
                    binding.button3.setBackgroundColor(Color.GRAY)
                    binding.button4.setBackgroundColor(Color.WHITE)
                    binding.button5.setBackgroundColor(Color.WHITE)
                }

                binding.button4.setOnClickListener {
                    if(userInfo != null) {
                        val updatedInfo = User(
                            userInfo.id, userInfo.name, userInfo.age, userInfo.gender,userInfo.city,
                            userInfo.country,userInfo.weight, userInfo.height, "Heavy",
                            userInfo.profilePictureLink, userInfo.latitude, userInfo.longitude
                        )

                        mUserViewModel.updateUser(updatedInfo)
                    }
                    binding.button.setBackgroundColor(Color.WHITE)
                    binding.button2.setBackgroundColor(Color.WHITE)
                    binding.button3.setBackgroundColor(Color.WHITE)
                    binding.button4.setBackgroundColor(Color.GRAY)
                    binding.button5.setBackgroundColor(Color.WHITE)
                }

                binding.button5.setOnClickListener {
                    if(userInfo != null) {
                        val updatedInfo = User(
                            userInfo.id, userInfo.name, userInfo.age, userInfo.gender,userInfo.city,
                            userInfo.country,userInfo.weight, userInfo.height, "Extreme",
                            userInfo.profilePictureLink, userInfo.latitude, userInfo.longitude
                        )

                        mUserViewModel.updateUser(updatedInfo)
                    }

                    binding.button.setBackgroundColor(Color.WHITE)
                    binding.button2.setBackgroundColor(Color.WHITE)
                    binding.button3.setBackgroundColor(Color.WHITE)
                    binding.button4.setBackgroundColor(Color.WHITE)
                    binding.button5.setBackgroundColor(Color.GRAY)
                }

            }

        }


    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, UserPortalMain::class.java)
        intent.putExtra("userID", userID)
        startActivity(intent)
    }



}