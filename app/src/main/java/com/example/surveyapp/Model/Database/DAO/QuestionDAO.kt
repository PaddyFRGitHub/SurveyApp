package com.example.surveyapp.Model.Database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.surveyapp.Model.Database.Question
import com.example.surveyapp.Model.Database.Survey

@Dao
interface QuestionDAO {
    @Insert
    suspend fun addQuestion(question: Question)

    @Query("SELECT * FROM Question where id = :id")
    suspend fun getQuestion(id: String): Question

    @Query("SELECT * FROM Question where surveyId = :surveyId")
    suspend fun getQuestionsForSurvey(surveyId: String): List<Question>
}