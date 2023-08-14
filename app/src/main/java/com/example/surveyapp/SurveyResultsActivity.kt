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

class SurveyResultsActivity : AppCompatActivity() {


    private val dbHelper: DataBaseHelper = DataBaseHelper(this)
    private val questions = ArrayList<Questions>()
    private var answersList = ArrayList<Answers>()
    private var questionIdList = ArrayList<Int>()
    private var resultList = ArrayList<com.example.surveyapp.Model.Choices>()
    private var surveyId = 0
    private lateinit var simpleList: ListView
    private var userId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey_results)
        supportActionBar?.title = ""

        surveyId = intent.getIntExtra("surveyid", 0)
        userId = intent.getIntExtra("userId", 0)

        val survey = dbHelper.getSurveyById(surveyId)
        val questions = dbHelper.getAllQuestionsBySurveyId(surveyId)

        questionIdList.addAll(questions.map { it.questionId })

        answersList =
            questionIdList.flatMap { dbHelper.getAllAnswersByQuestionid(it) } as ArrayList<Answers>
        val totalAnswers = answersList.size

        var j = 1

        try {
            for (questionId in questionIdList) {
                var strongAgree = 0
                var agree = 0
                var neither = 0
                var disagree = 0
                var strongDisagree = 0

                for (answer in answersList) {
                    if (questionId == answer.questionId) {
                        when (answer.answerText) {
                            "1.Strongly Agree" -> strongAgree++
                            "2.Agree" -> agree++
                            "3.Neither Agree Nor Disagree" -> neither++
                            "4.Disagree" -> disagree++
                            "5.Strongly Disagree" -> strongDisagree++
                        }
                    }
                }

                val a = strongAgree.toDouble() / (totalAnswers / 10) * 100
                val b = agree.toDouble() / (totalAnswers / 10) * 100
                val c = neither.toDouble() / (totalAnswers / 10) * 100
                val d = disagree.toDouble() / (totalAnswers / 10) * 100
                val e = strongDisagree.toDouble() / (totalAnswers / 10) * 100
                resultList.add(
                    Choices(
                        j++,
                        totalAnswers,
                        a.roundToInt(),
                        b.roundToInt(),
                        c.roundToInt(),
                        d.roundToInt(),
                        e.roundToInt()
                    )
                )
            }
        } catch (e: IllegalArgumentException) {
            for (f in 1..questionIdList.size) {
                resultList.add(Choices(f, 0, 0, 0, 0, 0, 0))
            }
        }

        findViewById<TextView>(R.id.text_editTitle).text = survey.surveyTitle

        simpleList = findViewById<ListView>(R.id.resultListView)
        val appAdaptor = Results(applicationContext, resultList)
        simpleList.adapter = appAdaptor
    }

    fun edit(view: View) {
        val intent = Intent(this, EditSurveyMainActivity::class.java)
        intent.putExtra("surveyId", surveyId)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }

    fun delete(view: View) {
        if (dbHelper.deleteSurvey(surveyId)) {
            val intent = Intent(this, AdminInterfaceActivity::class.java)

            try {
                for (question in questions.take(10)) {
                    dbHelper.deleteQuestion(question.questionId)
                }

                for (answer in answersList) {
                    dbHelper.deleteAnswer(answer)
                }
            } catch (e: java.lang.IndexOutOfBoundsException) {
            }

            intent.putExtra("userId", userId)
            Toast.makeText(this, "Survey Deleted!", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun goBack(view: View) {
        finish()
    }
}