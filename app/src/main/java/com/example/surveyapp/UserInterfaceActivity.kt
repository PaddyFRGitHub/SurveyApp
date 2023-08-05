package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.Model.DataBaseHelper
import com.example.surveyapp.Model.Managers.UserManager
import com.example.surveyapp.Model.Managers.UserSurveyManager

class UserInterfaceActivity : AppCompatActivity() {
    lateinit var simpleList: ListView

    val userManager = UserManager()
    val userSurveyManager = UserSurveyManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_interface)
        supportActionBar?.title = ""

        val user = userManager.currentUser.value

        findViewById<TextView>(R.id.textView9).text = user?.id

        val surveyList = userSurveyManager.getPublishedSurveys()
        simpleList = findViewById<ListView>(R.id.listviewItem2)

        val surveyListsAdaptor = SurveyLists(applicationContext, ArrayList(surveyList.map { it.survey }))

        simpleList!!.adapter = surveyListsAdaptor

        simpleList.isClickable = true
        simpleList.setOnItemClickListener { parent, view, positon, id ->
            val survey = surveyList[positon]

            val intent = Intent(this, UserAnswerActivity::class.java)
            intent.putExtra("surveyId", survey.survey.id)
            intent.putExtra("userId", user?.id)

            startActivity(intent)
        }
    }

    fun logOutBtn(view: View) {
        userManager.currentUser.value = null
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}