package com.example.lifestyle_kalyan

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.lifestyle_kalyan.databinding.ActivitySignUpBinding
import com.example.lifestyle_kalyan.databinding.ActivityUpdateUserInfoBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class UpdateUserInfo : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityUpdateUserInfoBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geoCoder: Geocoder
    private var name : String = ""
    private var age : Int? = null
    private var gender : String = ""
    private var city : String = ""
    private var country : String = ""
    private var height : Int? = null
    private var weight: Int? = null
    private var activityLevel: String = ""
    private var profilePictureLink: String = ""
    private var locationAvailable = false
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0
    private var userID: Int = 0
    private lateinit var userInfo: UserInfoModel

    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as UserApplication).repository)
    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user_info)

        binding = ActivityUpdateUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        userID = intent.getIntExtra("userID_toUpdatePage",-1)

        // Set selected user
        mUserViewModel.setSelectedUser(userID)
        mUserViewModel.userData.observe(this, userDataObserver)


    }

    private val userDataObserver: androidx.lifecycle.Observer<User> =
        androidx.lifecycle.Observer { userInfo ->
            if (userInfo != null) {
                binding.nameUpdate.setText(userInfo.name)
                binding.ageUpdate.setText(userInfo.age.toString())
                binding.weightUpdate.setText(userInfo.weight.toString())
                binding.heightUpdate.setText(userInfo.height.toString())

                println("++++++++++++++" + userID)


                // Gender spinner related
                val genderSpinner : Spinner = binding.genderUpdate

                ArrayAdapter.createFromResource(
                    this,
                    R.array.gender_list_array,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    genderSpinner.adapter = adapter
                }
                genderSpinner.onItemSelectedListener = this
                if(userInfo.gender == "Male") {
                    genderSpinner.setSelection(1)
                }
                else if(userInfo.gender == "Female") {
                    genderSpinner.setSelection(2)
                }


                // Activity level dropdown spinner related
                val activityLevelSpinner : Spinner = binding.activityLevelUpdate

                ArrayAdapter.createFromResource(
                    this,
                    R.array.activity_level_array,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    activityLevelSpinner.adapter = adapter
                }

                activityLevelSpinner.onItemSelectedListener = this
                if(userInfo.activityLevel == "Sedentary") {
                    activityLevelSpinner.setSelection(1)
                }
                else if(userInfo.activityLevel == "Mild") {
                    activityLevelSpinner.setSelection(2)
                }
                else if(userInfo.activityLevel == "Moderate") {
                    activityLevelSpinner.setSelection(3)
                }
                else if(userInfo.activityLevel == "Heavy") {
                    activityLevelSpinner.setSelection(4)
                }
                else if(userInfo.activityLevel == "Extreme") {
                    activityLevelSpinner.setSelection(5)
                }

                // Set image link
                binding.profilePictureUpdate.setImageURI(Uri.parse(userInfo.profilePictureLink))
                profilePictureLink = userInfo.profilePictureLink

                // Handles form submission
                binding.submitUpdate.setOnClickListener{
                    updateFormValues()
                }

                // Handles click on take profile picture
                binding.takePictureUpdate.setOnClickListener {
                    dispatchCapturePictureIntent()
                }

                // Location display
                binding.locationUpdate.text = "Location: ${userInfo.city}, ${userInfo.country}"
                locationAvailable = true
                city = userInfo.city
                country = userInfo.country
                latitude = userInfo.latitude
                longitude = userInfo.longitude

                // Handle click on change location
                binding.changeLocationButton.setOnClickListener {
                    handleChangeLocationClick()
                }
            }
        }

    private fun updateFormValues() {
        var hasError = false

        if(binding.nameUpdate.text.toString() != "") {
            name = binding.nameUpdate.text.toString()
        }
        else {
            hasError = true
        }

        if(binding.ageUpdate.text.toString() != "") {
            age = binding.ageUpdate.text.toString().toInt()
        }
        else {
            hasError = true
        }

        if(gender == "" || gender == "Select Gender") {
            hasError = true
        }

        if(profilePictureLink == "") {
            hasError = true
        }

        if(activityLevel == "" || activityLevel == "Select Activity Level") {
            hasError = true
        }

        if(city == "") {
            hasError = true
        }

        if(country == "") {
            hasError = true
        }

        if(binding.heightUpdate.text.toString() != "") {
            height = binding.heightUpdate.text.toString().toInt()
        }
        else {
            hasError = true
        }

        if(binding.weightUpdate.text.toString() != "") {
            weight = binding.weightUpdate.text.toString().toInt()
        }
        else {
            hasError = true
        }

        if(hasError) {
            Toast.makeText(baseContext, "Enter all the required fields to continue.", Toast.LENGTH_SHORT).show()
        }
        else {
            //
            val id = userID
            val userInfo = User(id, name, age, gender, city, country, weight, height, activityLevel, profilePictureLink, latitude, longitude)
            val returnedVal = mUserViewModel.updateUser(userInfo)

            println(returnedVal)

            val intent = Intent(this, UserPortalMain::class.java)
            intent.putExtra("userID", userInfo.id)
            startActivity(intent)

        }
    }

    fun getRandomID() : Int {
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        val input = parent!!.getItemAtPosition(pos)
        if(parent.tag == "activityLevelSpinner") {
            activityLevel = input as String
        }
        else if(parent.tag == "genderSpinner") {
            gender = input as String
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    fun dispatchCapturePictureIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        try {
            val file = createImageFile()
            file.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "com.example.lifestyle_kalyan.fileProvider",
                    it
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(intent, 1)
            }
        }
        catch (e: FileNotFoundException) {
            println(e)
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile(
            "IMG_${timeStamp}_",
            ".jpg",
            storageDir
        )
        file.apply { profilePictureLink = absolutePath }

        return file
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == RESULT_OK) {
            binding.profilePictureUpdate.setImageURI(Uri.parse(profilePictureLink))
            println(profilePictureLink)
        }
    }

    val locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission())
    { isGranted: Boolean ->

        if(isGranted) {
            // Location permission granted
            println("Location permission granted")
            checkForLocationPermissions()
        }
        else {
            // Location permission not granted
            println("Location permission not granted")
        }
    }

    fun checkForLocationPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED -> {

                println("Granted")

                fusedLocationClient.getCurrentLocation(
                    LocationRequest.PRIORITY_HIGH_ACCURACY, null
                )
                    .addOnSuccessListener{location: Location? ->
                        locationAvailable = true
                        if(location != null) {
                            latitude = location.latitude
                            longitude = location.longitude

                            var addressList = geoCoder.getFromLocation(latitude, longitude, 1)

                            country = addressList[0].countryName
                            city = addressList[0].locality
                            binding.locationUpdate.setText("Location: ${city}, ${country}")
                        }
                    }

            }
            else -> {
                println("Not granted")
                locationPermissionRequest.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION)
            }

        }
    }

    fun handleChangeLocationClick() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geoCoder = Geocoder(this)
        checkForLocationPermissions()
    }
}