package com.example.lifestyle_kalyan

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = arrayOf(User::class), version = 1, exportSchema = false)

public abstract class UserRoomDatabase : RoomDatabase() {

    abstract fun userDao() : UserDAO

    companion object {
        @Volatile
        private var INSTANCE: UserRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ) : UserRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    UserRoomDatabase::class.java, "users_v2").build()
                INSTANCE = instance
                instance
            }
        }
    }
}