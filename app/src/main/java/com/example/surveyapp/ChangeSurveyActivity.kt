package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.Model.*
import kotlin.math.roundToInt

class ChangeSurveyActivity : AppCompatActivity() {

    val dbHelper: DataBaseHelper = DataBaseHelper(this)
    val questions = ArrayList<Question>()
    var answerList = ArrayList<Answer>()
    var questionIdList = ArrayList<Int>()
    var resultList = ArrayList<com.example.surveyapp.Model.Choices>()
    var surveyid = 0
    lateinit var simpleList: ListView
    var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_survey)
        supportActionBar?.title = ""

        var getsurveid = intent.getIntExtra("surveyid", 0)
        var getuserid = intent.getIntExtra("userId", 0)
        surveyid = getsurveid
        userId = getuserid

        val survey = dbHelper.getSurveyById(surveyid)
        val answers = dbHelper.getAllAnswers()
        val questions = dbHelper.getAllQuestionsBySurveyId(surveyid)

        for (question in questions) {
            questionIdList.add(question.questionId)
        }

        for (id in questionIdList) {
            answerList.addAll(dbHelper.getAllAnswersByQuestionid(id))
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
                    Choices(
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
            var f = 1
            for (questionId in questionIdList) {
                resultList.add(Choices(f++, 0, 0, 0, 0, 0, 0))
            }
        }

        findViewById<TextView>(R.id.text_editTitle).text = survey.surveyTitle
        findViewById<TextView>(R.id.text_editStartDate).text = survey.surveyStartDate
        findViewById<TextView>(R.id.text_editEndDate).text = survey.surveyEndDate
        findViewById<TextView>(R.id.totalanswers).text =
            "Total responses " + (totalAnswers / 10).toString()

        simpleList = findViewById<ListView>(R.id.resultListView)

        val appAdaptor = Results(applicationContext, resultList)

        simpleList!!.adapter = appAdaptor

    }

    fun edit(view: View) {
        val intent = Intent(this, EditSurveyMainActivity::class.java)
        intent.putExtra("surveyId", surveyid)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }


    fun delete(view: View) {

        if (dbHelper.deleteSurvey(surveyid)) {
            val intent = Intent(this, AdminInterface::class.java)

            try {
                for (i in 0 until 10) {
                    dbHelper.deleteQuestion(questions[i].questionId)
                }

                for (i in 0 until answerList.size) {
                    dbHelper.deleteAnswer(answerList[i])
                }
            } catch (e: java.lang.IndexOutOfBoundsException) {
            }


            intent.putExtra("userId", userId)
            Toast.makeText(this, "Survey deleted", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun goBack(view: View) {
        finish()
    }
}