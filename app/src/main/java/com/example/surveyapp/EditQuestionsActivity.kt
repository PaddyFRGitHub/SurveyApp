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

class EditQuestionsActivity : AppCompatActivity() {
    private val dbHelper = DataBaseHelper(this)
    private val questionEditTexts = mutableListOf<EditText>()
    private var transferId2 = 0
    private val newArray = ArrayList<Questions>()
    private val questionsUpdateList = ArrayList<Questions>()
    private var userNo = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_questions)
        supportActionBar?.title = ""

        val transferId = intent.getIntExtra("surveyId", 0)
        userNo = intent.getIntExtra("userId", 0)
        transferId2 = transferId
        val questions = dbHelper.getAllQuestionsBySurveyId(transferId2)

        // Initialize the questionEditTexts list
        questionEditTexts.addAll(
            listOf(
                findViewById(R.id.text_question11),
                findViewById(R.id.text_question12),
                findViewById(R.id.text_question13),
                findViewById(R.id.text_question14),
                findViewById(R.id.text_question15),
                findViewById(R.id.text_question16),
                findViewById(R.id.text_question17),
                findViewById(R.id.text_question18),
                findViewById(R.id.text_question19),
                findViewById(R.id.text_question20)
            )
        )

        for (question in questions) {
            newArray.add(question)
        }

        // Set the text for the EditText views
        for (i in 0 until newArray.size) {
            questionEditTexts[i].setText(newArray[i].questionText)
        }
    }

    fun finish(view: View) {
        val title = intent.getStringExtra("title").toString()
        val startDate = intent.getStringExtra("startDate").toString()
        val endDate = intent.getStringExtra("endDate").toString()

        // Update the question texts from the EditText views
        for (i in 0 until newArray.size) {
            newArray[i].questionText = questionEditTexts[i].text.toString()
        }

        if (areQuestionsBlank()) {
            Toast.makeText(this, "Please fill in all the questions", Toast.LENGTH_SHORT).show()
            return
        } else {
            val survey = Survey(transferId2, title, startDate, endDate)
            if (dbHelper.updateSurvey(survey)) {
                Toast.makeText(this, "Survey updated", Toast.LENGTH_SHORT).show()

                for (i in 0 until newArray.size) {
                    dbHelper.updateQuestion(newArray[i])
                }

                val intent = Intent(this, AdminInterfaceActivity::class.java)
                intent.putExtra("userId", userNo)
                startActivity(intent)

            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun areQuestionsBlank(): Boolean {
        for (i in 0 until 10) {
            if (questionEditTexts[i].text.toString().isBlank()) {
                return true
            }
        }
        return false
    }

    fun goBack2(view: View) {
        finish()
    }
}

