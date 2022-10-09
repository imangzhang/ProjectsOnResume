package com.example.lifestyle_kalyan

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import com.example.lifestyle_kalyan.databinding.ActivityUserPortalMainBinding
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class UserPortalMain : AppCompatActivity() {
    private lateinit var binding : ActivityUserPortalMainBinding
    private lateinit var databaseHelper: DatabaseHelper
    lateinit var currentPhotoPath: String
    private lateinit var bmrCalcHelper: BMRCalcHelper
    private var mButtonWeather: Button? = null
    private lateinit var userInfo: UserInfoModel
    private  var userID: Int = 0
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as UserApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPortalMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //
        userID = intent.getIntExtra("userID", -1)
        println("USER ID: " + userID)

        // Set selected user
        mUserViewModel.setSelectedUser(userID)
        mUserViewModel.userData.observe(this, userDataObserver)

        binding.userPortalMap.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("geo:0, 0?q=hike")
            val chooser: Intent = Intent.createChooser(intent, "Launch Map")
            startActivity(chooser)
        }

        binding.BMRUserPortal.text = "BMI: ${mUserViewModel.userBMR}"

        binding.BMRUserPortal.setOnClickListener {
            val intent = Intent(this, CalculationActivity::class.java)
            intent.putExtra("userID", userID)
            startActivity(intent)
        }
    }

    private val userDataObserver: androidx.lifecycle.Observer<User> =
        androidx.lifecycle.Observer { userInfo ->
            if(userInfo != null) {
                binding.userNamePortalMain.text = userInfo.name
                binding.profilePicturePortalMain.setImageURI(Uri.parse(userInfo.profilePictureLink))
                bmrCalcHelper = BMRCalcHelper()
                var bmr = bmrCalcHelper.calculateBMR(userInfo.height, userInfo.weight, userInfo.age, userInfo.gender)
                bmr = bmrCalcHelper.calculateBMRWithActivityLevel(bmr, userInfo.activityLevel)
                binding.BMRUserPortal.text = "BMR: ${bmr.toString()}"
                latitude = userInfo.latitude
                longitude = userInfo.longitude
            }

        }


    fun navigateToWeatherPage(view: View){
        val intent = Intent(this, WeatherActivity::class.java)
        intent.putExtra("latitude", latitude)
        intent.putExtra("longitude", longitude)
        startActivity(intent)
    }

    fun navigateToUpdateUserInfoPage(view: View){
        val intent = Intent(this, UpdateUserInfo::class.java)
        intent.putExtra("userID_toUpdatePage", userID)
        startActivity(intent)
    }



}