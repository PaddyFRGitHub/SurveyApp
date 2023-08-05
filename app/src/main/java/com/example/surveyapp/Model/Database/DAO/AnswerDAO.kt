package com.example.surveyapp.Model.Database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.surveyapp.Model.Database.Answer

@Dao
interface AnswerDAO {
    @Query("SELECT * FROM Answer where id = :id")
    suspend fun getAnswer(id: String): Answer

    @Insert
    suspend fun addAnswer(survey: Answer)
}