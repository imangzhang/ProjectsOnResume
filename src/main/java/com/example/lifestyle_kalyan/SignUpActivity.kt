package com.example.lifestyle_kalyan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.lifestyle_kalyan.databinding.ActivitySignUpBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class SignUpActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivitySignUpBinding
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

    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as UserApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gender dropdown spinner related
        val genderSpinner : Spinner = binding.gendeSighUp

        ArrayAdapter.createFromResource(
            this,
            R.array.gender_list_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            genderSpinner.adapter = adapter
        }

        genderSpinner.onItemSelectedListener = this

        // Activity level dropdown spinner related
        val activityLevelSpinner : Spinner = binding.activityLevelSighUp

        ArrayAdapter.createFromResource(
            this,
            R.array.activity_level_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            activityLevelSpinner.adapter = adapter
        }

        activityLevelSpinner.onItemSelectedListener = this

        // Handles form submission
        binding.submitSighUp.setOnClickListener{
            updateFormValues()
        }

        // Handles click on take profile picture
        binding.takePictureSighUp.setOnClickListener {
            dispatchCapturePictureIntent()
        }

        // User location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geoCoder = Geocoder(this)
        checkForLocationPermissions()
    }

    private fun updateFormValues() {
        var hasError = false

        if(binding.nameSighUp.text.toString() != "") {
            name = binding.nameSighUp.text.toString()
        }
        else {
            hasError = true
        }

        if(binding.ageSignUp.text.toString() != "") {
            age = binding.ageSignUp.text.toString().toInt()
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

        if(binding.heightSighUp.text.toString() != "") {
            height = binding.heightSighUp.text.toString().toInt()
        }
        else {
            hasError = true
        }

        if(binding.weightSighUp.text.toString() != "") {
            weight = binding.weightSighUp.text.toString().toInt()
        }
        else {
            hasError = true
        }

        if(hasError) {
            Toast.makeText(baseContext, "Enter all the required fields to continue.", Toast.LENGTH_SHORT).show()
        }
        else {
            //
            val id = getRandomID()
            val userInfo = User(id, name, age, gender, city, country, weight, height, activityLevel, profilePictureLink, latitude, longitude)
            mUserViewModel.addUser(userInfo)

            val intent = Intent(this, MainActivity::class.java)
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
            binding.profilePictureSighUp.setImageURI(Uri.parse(profilePictureLink))
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
                            binding.locationSighUp.setText("Location: ${city}, ${country}")
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
}