package com.example.surveyapp.Model.Database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.surveyapp.Model.Database.Survey
import com.example.surveyapp.Model.Database.User

@Dao
interface UserDAO {
    @Insert
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM User where id = :id")
    suspend fun getUser(id: String): User?
}