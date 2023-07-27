package com.example.surveyapp.Model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.sql.SQLException

private val DataBaseName = "AppDatabase.db"
private val ver: Int = 1

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DataBaseName, null, ver) {

    /*User Table*/
    public val users = "Users"
    public val userId = "userId"
    public val userName = "userName"
    public val passWord = "passWord"
    public val isAdmin = "isAdmin"

    /*Surveys Table*/
    public val surveys = "Surveys"
    public val surveyId = "surveyId"
    public val surveyTitle = "surveyTitle"
    public val surveyStartDate = "surveyStartDate"
    public val surveyEndDate = "surveyEndDate"

    /*Questions Table*/
    public val questions = "Questions"
    public val questionId = "questionId"
    public val questionText = "questionText"
    public val questionSurveyId = "surveyId"

    /*Answers Table*/
    public val answers = "Answers"
    public val answerId = "answerId"
    public val answerQuestionId = "questionId"
    public val answerUserId = "userId"
    public val answerText = "answerText"

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            var sqlCreateStatement: String = "CREATE TABLE IF NOT EXISTS " + users + " ( " +
                    userId + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    userName + " TEXT NOT NULL, " +
                    passWord + " TEXT NOT NULL, " +
                    isAdmin + " INTEGER DEFAULT 0 )"

            db?.execSQL(sqlCreateStatement)

            sqlCreateStatement = "CREATE TABLE IF NOT EXISTS " +
                    surveys + " ( " +
                    surveyId + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    surveyTitle + " TEXT NOT NULL, " +
                    surveyStartDate + " TEXT NOT NULL, " +
                    surveyEndDate + " TEXT )"

            db?.execSQL(sqlCreateStatement)


            sqlCreateStatement = "CREATE TABLE IF NOT EXISTS " +
                    questions + " ( " +
                    questionId + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    questionText + " TEXT, " +
                    questionSurveyId + " INTEGER, " + " FOREIGN KEY ($questionSurveyId) REFERENCES $surveys($surveys))"

            db?.execSQL(sqlCreateStatement)

            sqlCreateStatement = "CREATE TABLE IF NOT EXISTS " +
                    answers + " ( " +
                    answerId + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    answerQuestionId + " INTEGER NOT NULL, " +
                    answerUserId + " INTEGER NOT NULL, " +
                    answerText + " TEXT, " + " FOREIGN KEY ($answerUserId) REFERENCES $answers ($answerId)," + " FOREIGN KEY ($answerQuestionId ) REFERENCES $questions($questionId))"

            db?.execSQL(sqlCreateStatement)


        } catch (e: SQLException) {
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


    // user getters
    fun getAllUsers(): ArrayList<User> {
        val userList = ArrayList<User>()
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $users"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            do {
                val userId = cursor.getInt(0)
                val userName = cursor.getString(1)
                val passWord = cursor.getString(2)
                val isAdmin = cursor.getInt(3)
                val x = User(userId, userName, passWord, isAdmin)
                userList.add(x)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return userList
    }

    fun getUserByID(uId: Int): User {
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $users WHERE $userId = $uId"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            db.close()
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

    fun getUser(uName: String): User {
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $users WHERE $userName = '$uName'"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            db.close()
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

        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(userName, user.userName)
        cv.put(passWord, user.passWord)
        cv.put(isAdmin, user.isAdmin)

        val success = db.insert(users, null, cv)
        db.close()
        return success != -1L

    }

    fun deleteUser(user: User): Boolean {

        val db: SQLiteDatabase = this.writableDatabase
        val result = db.delete(users, "$userId = ${user.userId}", null) == 1

        db.close()
        return result
    }

    fun updateUser(user: User): Boolean {

        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(userName, user.userName)
        cv.put(passWord, user.passWord)
        cv.put(isAdmin, user.isAdmin)

        val result = db.update(users, cv, "$userId = ${user.userId}", null) == 1
        db.close()
        return result
    }

    // survey getters

    fun getAllSurveys(): ArrayList<Survey> {
        val surveyList = ArrayList<Survey>()
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $surveys"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

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
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $surveys WHERE $surveyTitle = '$uName'"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

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
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $surveys WHERE $surveyId = $uId"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

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

        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(surveyTitle, survey.surveyTitle)
        cv.put(surveyStartDate, survey.surveyStartDate)
        cv.put(surveyEndDate, survey.surveyEndDate)

        val success = db.insert(surveys, null, cv)
        db.close()
        return success != -1L

    }

    fun deleteSurvey(survey: Int): Boolean {

        val db: SQLiteDatabase = this.writableDatabase
        val result = db.delete(surveys, "$surveyId = $survey", null) == 1

        db.close()
        return result
    }

    fun updateSurvey(survey: Survey): Boolean {

        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(surveyTitle, survey.surveyTitle)
        cv.put(surveyStartDate, survey.surveyStartDate)
        cv.put(surveyEndDate, survey.surveyEndDate)

        val result = db.update(surveys, cv, "$surveyId = ${survey.surveyId}", null) == 1
        db.close()
        return result
    }

    //question getters

    fun getAllQuestions(): ArrayList<Question> {
        val answersList = ArrayList<Question>()
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $questions"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            do {
                val questionId = cursor.getInt(0)
                val questionText = cursor.getString(1)
                val surveyId = cursor.getInt(2)
                val x = Question(questionId, questionText, surveyId)
                answersList.add(x)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return answersList
    }

    fun getAllQuestionsBySurveyId(id: Int): ArrayList<Question> {
        val questionList = ArrayList<Question>()
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $questions WHERE $questionSurveyId = $id"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            do {
                val questionId = cursor.getInt(0)
                val questionText = cursor.getString(1)
                val surveyId = cursor.getInt(2)
                val x = Question(questionId, questionText, surveyId)
                questionList.add(x)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return questionList
    }

    fun getQuestion(uId: Int): Question {
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $questions WHERE $questionId = $uId"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            db.close()
            return Question(cursor.getInt(0), cursor.getString(1), cursor.getInt(2))
        } else {
            db.close()
            return Question(0, "Error", 0)
        }
    }


    fun addQuestion(question: Question): Boolean {

        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(questionText, question.questionText)
        cv.put(questionSurveyId, question.surveyId)

        val success = db.insert(questions, null, cv)
        db.close()
        return success != -1L

    }

    fun deleteQuestion(question: Int): Boolean {

        val db: SQLiteDatabase = this.writableDatabase
        val result = db.delete(questions, "$questionId = $question", null) == 1

        db.close()
        return result
    }

    fun updateQuetion(question: Question): Boolean {

        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(questionText, question.questionText)
        cv.put(questionSurveyId, question.surveyId)

        val result = db.update(questions, cv, "$questionId = ${question.questionId}", null) == 1
        db.close()
        return result
    }

    //answer getters

    fun getAllAnswers(): ArrayList<Answer> {
        val answersList = ArrayList<Answer>()
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $answers"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            do {
                val answerId = cursor.getInt(0)
                val questionId = cursor.getInt(1)
                val userId = cursor.getInt(2)
                val answerText = cursor.getString(3)
                val x = Answer(answerId, questionId, userId, answerText)
                answersList.add(x)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return answersList
    }

    fun getAnswer(uId: Int): Answer {
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $answers WHERE $answerId = $uId"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            db.close()
            return Answer(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3))
        } else {
            db.close()
            return Answer(0, 0, 0, "error")
        }
    }

    fun getAnswerbyQuestionId(uId: Int): Answer {
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $answers WHERE $answerQuestionId = $uId"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            db.close()
            return Answer(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3))
        } else {
            db.close()
            return Answer(0, 0, 0, "error")
        }
    }

    fun getAllAnswersByQuestionid(uId: Int): ArrayList<Answer> {
        val answersList = ArrayList<Answer>()
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $answers WHERE $answerQuestionId = $uId"

        val cursor: Cursor = db.rawQuery(sqlStatement, null)

        if (cursor.moveToFirst()) {
            do {
                val answerId = cursor.getInt(0)
                val questionId = cursor.getInt(1)
                val userId = cursor.getInt(2)
                val answerText = cursor.getString(3)
                val x = Answer(answerId, questionId, userId, answerText)
                answersList.add(x)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return answersList
    }

    fun addAnswer(answer: Answer): Boolean {

        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(answerText, answer.answerText)
        cv.put(answerUserId, answer.userId)
        cv.put(answerQuestionId, answer.questionId)

        val success = db.insert(answers, null, cv)
        db.close()
        return success != -1L

    }

    fun deleteAnswer(answer: Answer): Boolean {

        val db: SQLiteDatabase = this.writableDatabase
        val result = db.delete(answers, "$answerId = ${answer.answerId}", null) == 1

        db.close()
        return result
    }


    fun updateAnswer(answer: Answer): Boolean {

        val db: SQLiteDatabase = this.writableDatabase
        val cv: ContentValues = ContentValues()

        cv.put(answerText, answer.answerText)
        cv.put(answerUserId, answer.userId)
        cv.put(answerQuestionId, answer.questionId)

        val result = db.update(answers, cv, "$answerId = ${answer.answerId}", null) == 1
        db.close()
        return result
    }

}