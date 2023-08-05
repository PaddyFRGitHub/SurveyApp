package com.example.surveyapp.Model.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.surveyapp.Model.Database.DAO.AnswerDAO
import com.example.surveyapp.Model.Database.DAO.QuestionDAO
import com.example.surveyapp.Model.Database.DAO.ResponsesDAO
import com.example.surveyapp.Model.Database.DAO.SurveyDAO
import com.example.surveyapp.Model.Database.DAO.UserDAO

@Database(entities = [
    Survey::class,
    Question::class,
    Answer::class,
    User::class,
    Response::class
 ], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDAO(): QuestionDAO
    abstract fun responsesDAO(): ResponsesDAO
    abstract fun surveyDAO(): SurveyDAO
    abstract fun userDAO(): UserDAO
    abstract fun answerDAO(): AnswerDAO
}

object Database {
    lateinit var db: AppDatabase

    fun createDatabase(context: Context) {
        if (!this::db.isInitialized) {
            db = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "survey-db"
            ).build()
        }
    }
}