package com.example.surveyapp.Model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.sql.SQLException

private val DataBaseName = "Surveydb.db"
private val ver: Int = 1

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DataBaseName, null, ver) {

    val users = "Users"
    val userId = "userId"
    val userName = "userName"
    val passWord = "passWord"
    val isAdmin = "isAdmin"

    val surveys = "Surveys"
    val surveyId = "surveyId"
    val surveyTitle = "surveyTitle"
    val surveyStartDate = "surveyStartDate"
    val surveyEndDate = "surveyEndDate"

    val questions = "Questions"
    val questionId = "questionId"
    val questionText = "questionText"
    val questionSurveyId = "surveyId"
    
    val answers = "Answers"
    val answerId = "answerId"
    val answerQuestionId = "questionId"
    val answerUserId = "userId"
    val answerText = "answerText"
    
    val completion = "Completion"
    val completionId = "completionId"
    val completionUserId = "userId"
    val completionSurveyId = "surveyId"


    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.apply {
                execSQL(
                    "CREATE TABLE IF NOT EXISTS $users (" +
                            "$userId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "$userName TEXT NOT NULL, " +
                            "$passWord TEXT NOT NULL, " +
                            "$isAdmin INTEGER DEFAULT 0)"
                )

                execSQL(
                    "CREATE TABLE IF NOT EXISTS $surveys (" +
                            "$surveyId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "$surveyTitle TEXT NOT NULL, " +
                            "$surveyStartDate TEXT NOT NULL, " +
                            "$surveyEndDate TEXT)"
                )

                execSQL(
                    "CREATE TABLE IF NOT EXISTS $questions (" +
                            "$questionId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "$questionText TEXT, " +
                            "$questionSurveyId INTEGER, " +
                            "FOREIGN KEY ($questionSurveyId) REFERENCES $surveys($surveys))"
                )

                execSQL(
                    "CREATE TABLE IF NOT EXISTS $answers (" +
                            "$answerId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "$answerQuestionId INTEGER NOT NULL, " +
                            "$answerUserId INTEGER NOT NULL, " +
                            "$answerText TEXT, " +
                            "FOREIGN KEY ($answerUserId) REFERENCES $users($userId), " +
                            "FOREIGN KEY ($answerQuestionId) REFERENCES $questions($questionId))"
                )

                execSQL(
                    "CREATE TABLE IF NOT EXISTS $completion (" +
                            "$completionId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "$completionUserId INTEGER NOT NULL, " +
                            "$completionSurveyId INTEGER NOT NULL, " +
                            "FOREIGN KEY ($completionUserId) REFERENCES $users($userId), " +
                            "FOREIGN KEY ($completionSurveyId) REFERENCES $surveys($surveyId))"
                )
            }
        } catch (e: SQLException) {
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }



    fun getUserByID(uId: Int): User {
        val db = this.readableDatabase
        val sqlStatement = "SELECT * FROM $users WHERE $userId = $uId"

        val cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            db.close()
            return User(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3)
            )
        } else {
            return User(0, "User not found", "Error", 0)
        }
    }

    fun getUser(uName: String): User {
        val db = this.readableDatabase
        val sqlStatement = "SELECT * FROM $users WHERE $userName = '$uName'"

        val cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            return User(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3)
            )
        } else {
            db.close()
            return User(0, "User not found", "Error", 0)
        }
    }

    fun addUser(user: User): Boolean {

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(userName, user.userName)
        cv.put(passWord, user.passWord)
        cv.put(isAdmin, user.isAdmin)

        val success = db.insert(users, null, cv)
        db.close()

        return success != -1L

    }

    fun getAllSurveys(): ArrayList<Survey> {
        val surveyList = ArrayList<Survey>()
        val db = this.readableDatabase
        val sqlStatement = "SELECT * FROM $surveys"

        val cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            do {
                val surveyId = cursor.getInt(0)
                val surveyTitle = cursor.getString(1)
                val surveyStartDate = cursor.getString(2)
                val surveyEndDate = cursor.getString(3)
                val x = Survey(surveyId, surveyTitle, surveyStartDate, surveyEndDate)
                surveyList.add(x)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return surveyList
    }

    fun getSurvey(uName: String): Survey {
        val db = this.readableDatabase
        val sanitizedTitle = uName.replace("'", "''")
        val sqlStatement = "SELECT * FROM $surveys WHERE $surveyTitle = '$sanitizedTitle'"

        val cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            db.close()
            return Survey(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
            )
        } else {
            db.close()
            return Survey(0, "User not found", "Error", "Error")
        }
    }

    fun getSurveyById(uId: Int): Survey {
        val db = this.readableDatabase
        val sqlStatement = "SELECT * FROM $surveys WHERE $surveyId = $uId"

        val cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            db.close()
            return Survey(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
            )
        } else {
            db.close()
            return Survey(0, "Survey not found", "error", "error")
        }
    }

    fun addSurvey(survey: Survey): Boolean {

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(surveyTitle, survey.surveyTitle)
        cv.put(surveyStartDate, survey.surveyStartDate)
        cv.put(surveyEndDate, survey.surveyEndDate)

        val success = db.insert(surveys, null, cv)
        db.close()
        return success != -1L

    }

    fun deleteSurvey(survey: Int): Boolean {

        val db = this.writableDatabase
        val result = db.delete(surveys, "$surveyId = $survey", null) == 1

        db.close()
        return result
    }

    fun updateSurvey(survey: Survey): Boolean {

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(surveyTitle, survey.surveyTitle)
        cv.put(surveyStartDate, survey.surveyStartDate)
        cv.put(surveyEndDate, survey.surveyEndDate)

        val result = db.update(surveys, cv, "$surveyId = ${survey.surveyId}", null) == 1
        db.close()
        return result
    }



    fun getAllQuestionsBySurveyId(id: Int): ArrayList<Questions> {
        val questionsList = ArrayList<Questions>()
        val db = this.readableDatabase
        val sqlStatement = "SELECT * FROM $questions WHERE $questionSurveyId = $id"

        val cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            do {
                val questionId = cursor.getInt(0)
                val questionText = cursor.getString(1)
                val surveyId = cursor.getInt(2)
                val x = Questions(questionId, questionText, surveyId)
                questionsList.add(x)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return questionsList
    }


    fun markSurveyAsCompleted(userId: Int, surveyId: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(completionUserId, userId)
            put(completionSurveyId, surveyId)
        }
        db.insert(completion, null, values)
        db.close()
    }


    fun isSurveyCompletedByUser(userId: Int, surveyId: Int): Boolean {
        val selectQuery = "SELECT * FROM $completion WHERE $completionUserId = $userId AND $completionSurveyId = $surveyId"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        val isCompleted = cursor.count > 0
        cursor.close()
        db.close()
        return isCompleted
    }


    fun addQuestion(questions: Questions): Boolean {

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(questionText, questions.questionText)
        cv.put(questionSurveyId, questions.surveyId)

        val success = db.insert(this.questions, null, cv)
        db.close()
        return success != -1L

    }

    fun deleteQuestion(question: Int): Boolean {

        val db = this.writableDatabase
        val result = db.delete(questions, "$questionId = $question", null) == 1

        db.close()
        return result
    }

    fun updateQuestion(questions: Questions): Boolean {

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(questionText, questions.questionText)
        cv.put(questionSurveyId, questions.surveyId)

        val result = db.update(this.questions, cv, "$questionId = ${questions.questionId}", null) == 1
        db.close()
        return result
    }



    fun getAllAnswers(): ArrayList<Answers> {
        val answersList = ArrayList<Answers>()
        val db = this.readableDatabase
        val sqlStatement = "SELECT * FROM $answers"

        val cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            do {
                val answerId = cursor.getInt(0)
                val questionId = cursor.getInt(1)
                val userId = cursor.getInt(2)
                val answerText = cursor.getString(3)
                val x = Answers(answerId, questionId, userId, answerText)
                answersList.add(x)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return answersList
    }

    fun getAllAnswersByQuestionid(uId: Int): ArrayList<Answers> {
        val answersList = ArrayList<Answers>()
        val db = this.readableDatabase
        val sqlStatement = "SELECT * FROM $answers WHERE $answerQuestionId = $uId"

        val cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            do {
                val answerId = cursor.getInt(0)
                val questionId = cursor.getInt(1)
                val userId = cursor.getInt(2)
                val answerText = cursor.getString(3)
                val x = Answers(answerId, questionId, userId, answerText)
                answersList.add(x)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return answersList
    }


    fun deleteAnswer(answers: Answers): Boolean {

        val db = this.writableDatabase
        val result = db.delete(this.answers, "$answerId = ${answers.answerId}", null) == 1

        db.close()
        return result
    }

    fun addAnswer(answers: Answers): Boolean {

        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(answerText, answers.answerText)
        cv.put(answerUserId, answers.userId)
        cv.put(answerQuestionId, answers.questionId)

        val success = db.insert(this.answers, null, cv)
        db.close()
        return success != -1L

    }
}