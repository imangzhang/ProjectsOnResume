package com.example.lifestyle_kalyan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lifestyle_kalyan.databinding.ActivityMainBinding
import kotlin.random.Random

// Background Image Source: https://pexels.com/photo/woman-in-white-t-shirt-walking-on-concrete-road-2792083


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var userNameSpinner : Spinner

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var binding: ActivityMainBinding
    private lateinit var userInfoList : LiveData<List<User>>
    private var selectedUserID: Int? = null

    private val mUserViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as UserApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set the observer for the vanilla livedata object
        mUserViewModel.allUsers.observe(this, liveDataObserver)
        userNameSpinner = binding.userNameSignIn

    }

    fun navigateToSignUpPage(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private val liveDataObserver: Observer<List<User>> =
    Observer { allUsers ->
        if (allUsers != null) {
            val userNameList = ArrayList<String>()

            for (userInfo in allUsers) {
                userNameList.add(userInfo.name)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userNameList)

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            userNameSpinner.adapter = adapter

            userNameSpinner.onItemSelectedListener = this
        } else {
            userNameSpinner.visibility = View.GONE
        }

    }

    fun navigateToUserPortalMain(view: View) {
        val intent = Intent(this, UserPortalMain::class.java)
        intent.putExtra("userID", mUserViewModel.selectedUserID)
        startActivity(intent)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        val userNameInput = parent!!.getItemAtPosition(pos) as String
        mUserViewModel.getUserByPos(pos)
        println(pos)
        println(userNameInput)
        println(selectedUserID)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}