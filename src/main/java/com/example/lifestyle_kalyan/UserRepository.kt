package com.example.lifestyle_kalyan

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDAO) {

    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    var selectedUserInfo = MutableLiveData<User>()

//    val userByID: Flow<User> = userDao.getUserByID(id: Int)

    @WorkerThread
    suspend fun getUserByPos(pos: Int): User {
        return userDao.getUserByPos(pos)
    }

    @WorkerThread
    suspend fun getUserByID(id: Int) {
        val userInfo = userDao.getUserByID(id)
        selectedUserInfo.postValue(userInfo)
    }


    @WorkerThread
    suspend fun addUser(user: User) {
        userDao.addUser(user)

    }

    @WorkerThread
    suspend fun updateUser(user: User) {
        userDao.updateUser(user)

    }
}