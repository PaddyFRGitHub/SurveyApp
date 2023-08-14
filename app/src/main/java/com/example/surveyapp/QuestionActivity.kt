package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.Model.DataBaseHelper
import com.example.surveyapp.Model.Questions
import com.example.surveyapp.Model.Survey

class QuestionActivity : AppCompatActivity() {

    private val dbHelper = DataBaseHelper(this)
    private val questionsList = ArrayList<Questions>()
    private var userNo4 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        supportActionBar?.title = ""
        userNo4 = intent.getIntExtra("userId", 0)
    }

    fun publish(view: View) {
        val title = intent.getStringExtra("title").toString()
        val startDate = intent.getStringExtra("startDate").toString()
        val endDate = intent.getStringExtra("endDate").toString()

        val questionEditTexts = arrayOf(
            R.id.text_question, R.id.text_question2, R.id.text_question3,
            R.id.text_question4, R.id.text_question5, R.id.text_question6,
            R.id.text_question7, R.id.text_question8, R.id.text_question9,
            R.id.text_question10
        )

        for (questionEditTextId in questionEditTexts) {
            val questionText = findViewById<EditText>(questionEditTextId).text.toString()
            if (questionText.isBlank()) {
                Toast.makeText(this, "Please fill in all the questions", Toast.LENGTH_SHORT).show()
                return
            }
            questionsList.add(Questions(0, questionText, 0))
        }

        val survey = Survey(0, title, startDate, endDate)

        if (dbHelper.addSurvey(survey)) {
            Toast.makeText(this, "Survey created", Toast.LENGTH_SHORT).show()

            val surveyFinder = dbHelper.getSurvey(survey.surveyTitle)
            val surveyId = surveyFinder.surveyId

            for (i in 0 until questionsList.size) {
                questionsList[i].surveyId = surveyId
                dbHelper.addQuestion(questionsList[i])
            }

            val intent = Intent(this, AdminInterfaceActivity::class.java)
            intent.putExtra("userId", userNo4)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Error: The user not added", Toast.LENGTH_SHORT).show()
        }
    }

    fun goBack(view: View) {
        finish()
    }
}