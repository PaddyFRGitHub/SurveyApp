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
    val dbHelper: DataBaseHelper = DataBaseHelper(this)
    val newArray = ArrayList<Questions>()
    val answersTextArray = ArrayList<String>()
    var surveyId = 0
    var userId = 0
    var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_answer)
        supportActionBar?.title = ""

        var getsurveyid = intent.getIntExtra("surveyId", 0)
        var getuserid = intent.getIntExtra("userId", 0)

        surveyId = getsurveyid
        userId = getuserid

        val questions = dbHelper.getAllQuestionsBySurveyId(surveyId)

        for (question in questions) {
            newArray.add(question)
        }

        findViewById<TextView>(R.id.text_Question).text = newArray[0].questionText

        findViewById<Button>(R.id.btn_finishAnswer).isVisible = false
        findViewById<Button>(R.id.btn_Previous).isVisible = false

        checkSelected()
    }

    fun nextQuestion(view: View) {
        val button1 = findViewById<RadioButton>(R.id.radioButton)
        val button2 = findViewById<RadioButton>(R.id.radioButton2)
        val button3 = findViewById<RadioButton>(R.id.radioButton3)
        val button4 = findViewById<RadioButton>(R.id.radioButton4)
        val button5 = findViewById<RadioButton>(R.id.radioButton5)

        if (!button1.isChecked &&
            !button2.isChecked &&
            !button3.isChecked &&
            !button4.isChecked &&
            !button5.isChecked
        ) {
            Toast.makeText(this, "Please check one option", Toast.LENGTH_SHORT).show()
            return
        }
        checkSelected()
        if (index + 1 != newArray.size) {
            index++
            findViewById<TextView>(R.id.text_Question).text = newArray[index].questionText
            findViewById<Button>(R.id.btn_Previous).isVisible = true
        }

        if (index + 1 == newArray.size) {
            findViewById<Button>(R.id.btn_finishAnswer).isVisible = true
            findViewById<Button>(R.id.btn_NextQuestion).isVisible = false
        }
    }

    fun previousQuestion(view: View) {
        if (index + 1 <= newArray.size) {
            findViewById<Button>(R.id.btn_finishAnswer).isVisible = false
            findViewById<Button>(R.id.btn_NextQuestion).isVisible = true
        }

        if (index + 1 != newArray.size || index + 1 == newArray.size) {
            index--
            answersTextArray.removeAt(index)
            findViewById<TextView>(R.id.text_Question).text = newArray[index].questionText
        }

        if (index + 1 == 1) {
            findViewById<Button>(R.id.btn_Previous).isVisible = false
        }
    }

    fun complete(view: View) {
        val button1 = findViewById<RadioButton>(R.id.radioButton)
        val button2 = findViewById<RadioButton>(R.id.radioButton2)
        val button3 = findViewById<RadioButton>(R.id.radioButton3)
        val button4 = findViewById<RadioButton>(R.id.radioButton4)
        val button5 = findViewById<RadioButton>(R.id.radioButton5)

        if (!button1.isChecked &&
            !button2.isChecked &&
            !button3.isChecked &&
            !button4.isChecked &&
            !button5.isChecked
        ) {
            Toast.makeText(this, "Please check one option", Toast.LENGTH_SHORT).show()
            return
        }
        checkSelected()

        for (i in 0 until 10) {
            dbHelper.addAnswer(Answers(0, newArray[i].questionId, userId, answersTextArray[i]))
        }

        intent.putExtra("userId", userId)
        startActivity(intent)

        dbHelper.markSurveyAsCompleted(userId, surveyId)

        val intent = Intent(this, UserInterfaceActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }

    fun checkSelected() {
        val button1 = findViewById<RadioButton>(R.id.radioButton)
        val button2 = findViewById<RadioButton>(R.id.radioButton2)
        val button3 = findViewById<RadioButton>(R.id.radioButton3)
        val button4 = findViewById<RadioButton>(R.id.radioButton4)
        val button5 = findViewById<RadioButton>(R.id.radioButton5)

        if (button1.isChecked) {
            answersTextArray.add(button1.text.toString())
        }
        if (button2.isChecked) {
            answersTextArray.add(button2.text.toString())
        }
        if (button3.isChecked) {
            answersTextArray.add(button3.text.toString())
        }
        if (button4.isChecked) {
            answersTextArray.add(button4.text.toString())
        }
        if (button5.isChecked) {
            answersTextArray.add(button5.text.toString())
        }
        findViewById<RadioGroup>(R.id.radioGroup).clearCheck()
    }

    fun cancelAnswers(view: View) {
        val intent = Intent(this, UserInterfaceActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }

}