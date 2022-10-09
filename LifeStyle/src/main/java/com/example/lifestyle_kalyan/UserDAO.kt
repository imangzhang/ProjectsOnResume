package com.example.lifestyle_kalyan

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {

    @Query("SELECT * from users_v2")
    fun getAllUsers() : Flow<List<User>>

    @Query("SELECT * from users_v2 WHERE id LIKE :id")
    fun getUserByID(id: Int) : User

    @Query("SELECT * from users_v2 LIMIT 1 offset :pos")
    fun getUserByPos(pos: Int) : User

    @Update
    suspend fun updateUser(user: User)

    @Insert
    fun addUser(user: User)


}