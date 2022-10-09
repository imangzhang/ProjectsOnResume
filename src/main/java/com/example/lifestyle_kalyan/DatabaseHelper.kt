package com.example.lifestyle_kalyan
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val TABLE_NAME = "user_data"
        private const val DATABASE_NAME = "Lifestyle"
        private const val DATABASE_VERSION = 10
        private const val ID = "id"
        private const val NAME = "name"
        private const val AGE = "age"
        private const val GENDER = "gender"
        private const val CITY = "city"
        private const val COUNTRY = "country"
        private const val WEIGHT = "weight"
        private const val HEIGHT = "height"
        private const val ACTIVITY_LEVEL = "activity_level"
        private const val PROFILE_PICTURE_LINK = "profile_picture_link"
        private const val LATITUDE = "latitude"
        private const val LONGITUDE = "longitude"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        var createUserInfoTableQuery =
            "CREATE TABLE ${TABLE_NAME} (" +
                    "${ID} INTEGER_PRIMARY_KEY," +
                    "${NAME} Text," +
                    "${AGE} INTEGER," +
                    "${GENDER} TEXT," +
                    "${CITY} TEXT," +
                    "${COUNTRY} TEXT," +
                    "${WEIGHT} INTEGER," +
                    "${HEIGHT} INTEGER," +
                    "${ACTIVITY_LEVEL} TEXT," +
                    "${PROFILE_PICTURE_LINK} TEXT," +
                    "${LATITUDE} DOUBLE," +
                    "${LONGITUDE} DOUBLE)"

        db?.execSQL(createUserInfoTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addUser(userInfo: UserInfoModel): Long? {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(ID, userInfo.id)
            put(NAME, userInfo.name)
            put(AGE, userInfo.age)
            put(GENDER, userInfo.gender)
            put(CITY, userInfo.city)
            put(COUNTRY, userInfo.country)
            put(WEIGHT, userInfo.weight)
            put(HEIGHT, userInfo.height)
            put(ACTIVITY_LEVEL, userInfo.activityLevel)
            put(PROFILE_PICTURE_LINK, userInfo.profilePictureLink)
            put(LATITUDE, userInfo.latitude)
            put(LONGITUDE, userInfo.longitude)
        }

        val returnedValue = db?.insert(TABLE_NAME, null, values)
        db.close()

        return returnedValue
    }

    fun getAllUsers(): ArrayList<UserInfoModel>  {
        val userInfoList : ArrayList<UserInfoModel> = ArrayList()
        val db = this.readableDatabase
        val selectQuery = "SELECT * from $TABLE_NAME"

        val cursor = db.rawQuery(selectQuery, null)

        with(cursor) {
            while(moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val name = getString(getColumnIndexOrThrow("name"))
                val age = getInt(getColumnIndexOrThrow("age"))
                val gender = getString(getColumnIndexOrThrow("gender"))
                val city = getString(getColumnIndexOrThrow("city"))
                val country = getString(getColumnIndexOrThrow("country"))
                val weight = getInt(getColumnIndexOrThrow("weight"))
                val height = getInt(getColumnIndexOrThrow("height"))
                val activityLevel = getString(getColumnIndexOrThrow("activity_level"))
                val profilePictureLink = getString(getColumnIndexOrThrow("profile_picture_link"))
                val latitude = getDouble(getColumnIndexOrThrow("latitude"))
                val longitude = getDouble(getColumnIndexOrThrow("longitude"))

                val userInfo = UserInfoModel(
                    id, name, age, gender, city, country, weight, height, activityLevel,
                    profilePictureLink, latitude, longitude
                )
                userInfoList.add(userInfo)
            }
        }

        return userInfoList
    }

    fun getUserByID(id: Int) : UserInfoModel? {
        val userInfoList = getAllUsers()

        for(userInfo in userInfoList) {
            if(userInfo.id == id) {
                return userInfo
            }
        }

        return null
    }

    fun updateUserInfo(userInfo: UserInfoModel): Int? {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(ID, userInfo.id)
            put(NAME, userInfo.name)
            put(AGE, userInfo.age)
            put(GENDER, userInfo.gender)
            put(CITY, userInfo.city)
            put(COUNTRY, userInfo.country)
            put(WEIGHT, userInfo.weight)
            put(HEIGHT, userInfo.height)
            put(ACTIVITY_LEVEL, userInfo.activityLevel)
            put(PROFILE_PICTURE_LINK, userInfo.profilePictureLink)
            put(LATITUDE, userInfo.latitude)
            put(LONGITUDE, userInfo.longitude)
        }

        val returnedValue = db?.update(TABLE_NAME, values, "id=?", arrayOf(userInfo.id.toString()))
        db.close()

        return returnedValue
    }
}