package com.example.surveyapp.Model.Database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.surveyapp.Model.Database.Response

@Dao
interface ResponsesDAO {
    @Insert
    suspend fun addResponse(response: Response)

    @Query("SELECT * FROM Response where surveyId = :surveyId")
    suspend fun getResponsesForSurvey(surveyId: String): List<Response>
}