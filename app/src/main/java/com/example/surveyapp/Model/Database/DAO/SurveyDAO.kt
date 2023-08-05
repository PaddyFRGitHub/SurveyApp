package com.example.surveyapp.Model.Database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.surveyapp.Model.Database.Question
import com.example.surveyapp.Model.Database.Survey

@Dao
interface SurveyDAO {
    @Insert
    suspend fun addSurvey(survey: Survey)

    @Update
    suspend fun updateSurvey(survey: Survey)

    @Query(
        "SELECT * FROM Survey " +
        "JOIN Question ON Survey.id = Question.surveyId where Survey.id = :id"
    )
    suspend fun getSurvey(id: String): Map<Survey, List<Question>>

    @Query("SELECT * FROM Survey")
    suspend fun getAllSurveys(): List<Survey>
}
