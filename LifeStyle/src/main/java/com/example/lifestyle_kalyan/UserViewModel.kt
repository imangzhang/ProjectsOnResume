package com.example.lifestyle_kalyan

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class UserViewModel(private val repository : UserRepository) : ViewModel() {

    val allUsers: LiveData<List<User>> = repository.allUsers.asLiveData()
    private val selectedUserInfo: LiveData<User> = repository.selectedUserInfo

    var selectedUserID : Int = -1
    var userBMR: Int = -1

    fun addUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        repository.addUser(user)
    }

    fun setSelectedUser(id: Int) = CoroutineScope(Dispatchers.IO).launch {
        selectedUserID = id
        repository.getUserByID(id)
    }

    fun getUserByPos(pos: Int) = CoroutineScope(Dispatchers.IO).launch {
        val user = repository.getUserByPos(pos)
        println("USER__ ID: " + user.id)
        selectedUserID = user.id
    }

    fun updateUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        repository.updateUser(user)
    }

    val userData: LiveData<User>
        get() = selectedUserInfo

}

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) : T {
        if(modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}