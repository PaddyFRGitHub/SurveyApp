package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.surveyapp.Model.Answers
import com.example.surveyapp.Model.DataBaseHelper
import com.example.surveyapp.Model.Questions

class UserAnswerActivity : AppCompatActivity() {

    private val dbHelper: DataBaseHelper = DataBaseHelper(this)
    private val newArray = ArrayList<Questions>()
    private val answersTextArray = ArrayList<String>()
    private var surveyId = 0
    private var userId = 0
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_answer)
        supportActionBar?.title = ""

        surveyId = intent.getIntExtra("surveyId", 0)
        userId = intent.getIntExtra("userId", 0)

        newArray.addAll(dbHelper.getAllQuestionsBySurveyId(surveyId))

        findViewById<TextView>(R.id.text_Question).text = newArray[0].questionText

        findViewById<Button>(R.id.btn_finishAnswer).isVisible = false
        findViewById<Button>(R.id.btn_Previous).isVisible = false

        checkSelected()
    }

    fun nextQuestion(view: View) {
        val selectedRadioButton = findViewById<RadioGroup>(R.id.radioGroup).checkedRadioButtonId
        if (selectedRadioButton == -1) {
            Toast.makeText(this, "Please check one option", Toast.LENGTH_SHORT).show()
            return
        }
        checkSelected()

        index++
        if (index < newArray.size) {
            findViewById<TextView>(R.id.text_Question).text = newArray[index].questionText
            findViewById<Button>(R.id.btn_Previous).isVisible = true
        }

        if (index == newArray.size - 1) {
            findViewById<Button>(R.id.btn_finishAnswer).isVisible = true
            findViewById<Button>(R.id.btn_NextQuestion).isVisible = false
        }
    }

    fun previousQuestion(view: View) {
        if (index >= 0) {
            findViewById<Button>(R.id.btn_finishAnswer).isVisible = false
            findViewById<Button>(R.id.btn_NextQuestion).isVisible = true
        }

        index--
        if (index >= 0) {
            answersTextArray.removeAt(index)
            findViewById<TextView>(R.id.text_Question).text = newArray[index].questionText
        }

        if (index == 0) {
            findViewById<Button>(R.id.btn_Previous).isVisible = false
        }
    }

    fun complete(view: View) {
        val selectedRadioButton = findViewById<RadioGroup>(R.id.radioGroup).checkedRadioButtonId
        if (selectedRadioButton == -1) {
            Toast.makeText(this, "Please check one option", Toast.LENGTH_SHORT).show()
            return
        }
        checkSelected()

        for (i in 0 until minOf(10, newArray.size)) {
            dbHelper.addAnswer(Answers(0, newArray[i].questionId, userId, answersTextArray[i]))
        }

        dbHelper.markSurveyAsCompleted(userId, surveyId)

        val intent = Intent(this, UserInterfaceActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }

    fun checkSelected() {
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val selectedRadioButton = findViewById<RadioButton>(radioGroup.checkedRadioButtonId)

        if (selectedRadioButton != null) {
            answersTextArray.add(selectedRadioButton.text.toString())
            radioGroup.clearCheck()
        }
    }

    fun cancelAnswers(view: View) {
        val intent = Intent(this, UserInterfaceActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }
}