package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.Model.DataBaseHelper
import com.example.surveyapp.Model.Question
import com.example.surveyapp.Model.Survey

class QuestionPanelActivity : AppCompatActivity() {

    val dbHelper = DataBaseHelper(this)
    val questionList = ArrayList<Question>()
    var userIddd = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_panel)
        supportActionBar?.title = ""
        var getuserid = intent.getIntExtra("userId", 0)

        userIddd = getuserid
    }

    fun publish(view: View) {

        val title = intent.getStringExtra("title").toString()
        val startDate = intent.getStringExtra("startDate").toString()
        val endDate = intent.getStringExtra("endDate").toString()


        val question1 = findViewById<EditText>(R.id.text_question).text.toString()
        val question2 = findViewById<EditText>(R.id.text_question2).text.toString()
        val question3 = findViewById<EditText>(R.id.text_question3).text.toString()
        val question4 = findViewById<EditText>(R.id.text_question4).text.toString()
        val question5 = findViewById<EditText>(R.id.text_question5).text.toString()
        val question6 = findViewById<EditText>(R.id.text_question6).text.toString()
        val question7 = findViewById<EditText>(R.id.text_question7).text.toString()
        val question8 = findViewById<EditText>(R.id.text_question8).text.toString()
        val question9 = findViewById<EditText>(R.id.text_question9).text.toString()
        val question10 = findViewById<EditText>(R.id.text_question10).text.toString()


        if (question1.isBlank() ||
            question2.isBlank() ||
            question3.isBlank() ||
            question4.isBlank() ||
            question5.isBlank() ||
            question6.isBlank() ||
            question7.isBlank() ||
            question8.isBlank() ||
            question9.isBlank() ||
            question10.isBlank()
        ) {
            Toast.makeText(this, "Please fill in all the questions", Toast.LENGTH_SHORT).show()
            return
        } else {

            val survey = Survey(0, title, startDate, endDate)


            if (dbHelper.addSurvey(survey)) {
                Toast.makeText(this, "Survey created", Toast.LENGTH_SHORT).show()


                val surveyFinder = dbHelper.getSurvey(survey.surveyTitle)
                val surveyId = surveyFinder.surveyId

                questionList.add(Question(0, question1, surveyId))
                questionList.add(Question(0, question2, surveyId))
                questionList.add(Question(0, question3, surveyId))
                questionList.add(Question(0, question4, surveyId))
                questionList.add(Question(0, question5, surveyId))
                questionList.add(Question(0, question6, surveyId))
                questionList.add(Question(0, question7, surveyId))
                questionList.add(Question(0, question8, surveyId))
                questionList.add(Question(0, question9, surveyId))
                questionList.add(Question(0, question10, surveyId))

                for (i in 0 until 10) {
                    dbHelper.addQuestion(questionList[i])
                }

                val intent = Intent(this, AdminInterfaceActivity::class.java)
                intent.putExtra("userId",userIddd)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Error: The user not added", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun goBack(view: View) {
        finish()
    }

}