package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.Model.DataBaseHelper

class UserInterfaceActivity : AppCompatActivity() {
    lateinit var simpleList: ListView


    val dbHelper: DataBaseHelper = DataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_interface)
        supportActionBar?.title = ""

        val userId = intent.getIntExtra("userId", 0)

        val displayUser = dbHelper.getUserByID(userId)
        findViewById<TextView>(R.id.textView9).text = displayUser.userName

        val surveyList = dbHelper.getAllSurveys()
        simpleList = findViewById<ListView>(R.id.listviewItem2)

        val surveyListsAdaptor = SurveyLists(applicationContext, surveyList)

        simpleList!!.adapter = surveyListsAdaptor

        simpleList.isClickable = true
        simpleList.setOnItemClickListener { parent, view, positon, id ->
            val surveyTitle = surveyList[positon]


            val intent = Intent(this, UserAnswerActivity::class.java)
            intent.putExtra("surveyId", surveyTitle.surveyId)
            intent.putExtra("userId", userId)


            startActivity(intent)
        }


        simpleList.setOnItemClickListener { parent, view, positon, id ->
            val surveyTitle = surveyList[positon]
            val surveyId = surveyTitle.surveyId

            val intent = if (dbHelper.isSurveyCompletedByUser(userId, surveyId)) {

                Toast.makeText(this, "You have already completed this survey.", Toast.LENGTH_SHORT).show()
                null
            } else {

                Intent(this, UserAnswerActivity::class.java).apply {
                    putExtra("surveyId", surveyId)
                    putExtra("userId", userId)
                }
            }

            intent?.let { startActivity(it) }
        }
    }

    fun logOutBtn(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}