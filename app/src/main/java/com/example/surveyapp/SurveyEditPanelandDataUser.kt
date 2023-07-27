package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.surveyapp.Model.Answer
import com.example.surveyapp.Model.DataBaseHelper
import com.example.surveyapp.Model.Result
import com.example.surveyapp.Model.Results
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class SurveyEditPanelandDataUser : AppCompatActivity() {
    val dbHelper: DataBaseHelper = DataBaseHelper(this)

    var surveyid = 0
    var transferUserId = 0
    var alreadyAnswered = ArrayList<Int>()
    var questionIdList = ArrayList<Int>()
    var answerList = ArrayList<Answer>()
    var resultList = ArrayList<com.example.surveyapp.Model.Result>()
    lateinit var simpleList: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey_edit_paneland_data_user)
        supportActionBar?.title = ""

        var getsurveyId = intent.getIntExtra("surveyid", 0)
        var getuserid = intent.getIntExtra("userId", 0)
        surveyid = getsurveyId
        transferUserId = getuserid

        val survey = dbHelper.getSurveyById(surveyid)
        val answers = dbHelper.getAllAnswers()
        val questions = dbHelper.getAllQuestionsBySurveyId(surveyid)
        findViewById<LinearLayout>(R.id.linearLayout).isVisible = false
        findViewById<TextView>(R.id.textView17).isVisible = false
        findViewById<TextView>(R.id.textView22).isVisible = false
        findViewById<TextView>(R.id.textView19).isVisible = false
        findViewById<TextView>(R.id.textView18).isVisible = false
        findViewById<TextView>(R.id.textView23).isVisible = false

        for (question in questions) {
            questionIdList.add(question.questionId)
        }

        for (id in questionIdList) {
            answerList.addAll(dbHelper.getAllAnswersByQuestionid(id))
        }

        for (id in 0 until answerList.size) {
            alreadyAnswered.add(answerList[id].userId)
        }

        var totalAnswers = answerList.size

        var j = 1

        try {
            for (questionId in questionIdList) {
                var strongAgree = 0
                var agree = 0
                var neither = 0
                var disagre = 0
                var strongDisagree = 0
                for (x in 0 until answerList.size) {
                    if (questionId == answerList[x].questionId) {
                        when (answerList[x].answerText) {
                            "1.Strongly Agree" -> {
                                strongAgree++
                            }
                            "2.Agree" -> {
                                agree++
                            }
                            "3.Neither Agree nor Disagree" -> {
                                neither++
                            }
                            "4.Disagree" -> {
                                disagre++
                            }
                            "5.Strongly Disagree" -> {
                                strongDisagree++
                            }
                        }
                    }
                }
                var a = strongAgree.toDouble() / (totalAnswers / 10) * 100
                var b = agree.toDouble() / (totalAnswers / 10) * 100
                var c = neither.toDouble() / (totalAnswers / 10) * 100
                var d = disagre.toDouble() / (totalAnswers / 10) * 100
                var x = strongDisagree.toDouble() / (totalAnswers / 10) * 100
                resultList.add(
                    Result(
                        j++,
                        totalAnswers,
                        a.roundToInt(),
                        b.roundToInt(),
                        c.roundToInt(),
                        d.roundToInt(),
                        x.roundToInt()
                    )
                )
            }
        } catch (e: IllegalArgumentException) {
            for (questionId in questionIdList) {
                resultList.add(Result(j, 0, 0, 0, 0, 0, 0))
            }
        }

        findViewById<TextView>(R.id.text_editTitle2).text = survey.surveyTitle
        var startdate = findViewById<TextView>(R.id.text_editStartDate2)
        startdate.text = survey.surveyStartDate
        var endDate = findViewById<TextView>(R.id.text_editEndDate2)
        endDate.text = survey.surveyEndDate

        val current: LocalDate = LocalDate.now()
        val localDate2: LocalDate =
            LocalDate.parse(endDate.text, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        val localDate3: LocalDate =
            LocalDate.parse(startdate.text, DateTimeFormatter.ofPattern("dd-MM-yyyy"))


        findViewById<TextView>(R.id.text_warning).text =
            "Data will be updated once the survey has been answered."

        if (localDate3 > current) {
            findViewById<Button>(R.id.btn_edit2).isVisible = false
            findViewById<TextView>(R.id.text_warning).isVisible = true
            findViewById<TextView>(R.id.text_warning).text = "The Survey has yet to start"
        }
        if (alreadyAnswered.contains(transferUserId) || localDate2 <= current || localDate3 < current) {
            findViewById<TextView>(R.id.text_warning).isVisible = false
            findViewById<Button>(R.id.btn_edit2).isVisible = false
            findViewById<LinearLayout>(R.id.linearLayout).isVisible = true
            findViewById<TextView>(R.id.textView17).isVisible = true
            findViewById<TextView>(R.id.textView22).isVisible = true
            findViewById<TextView>(R.id.textView19).isVisible = true
            findViewById<TextView>(R.id.textView18).isVisible = true
            findViewById<TextView>(R.id.textView23).isVisible = true

        }
        simpleList = findViewById<ListView>(R.id.resultListView)

        val appAdaptor = Results(applicationContext, resultList)

        simpleList!!.adapter = appAdaptor


    }

    fun answer(view: View) {
        val intent = Intent(this, UserAnswerPanel::class.java)
        intent.putExtra("surveyid", surveyid)
        intent.putExtra("userId", transferUserId)
        startActivity(intent)
    }


    fun goBack(view: View) {
        finish()
    }

    override fun onBackPressed() {
        return
    }
}