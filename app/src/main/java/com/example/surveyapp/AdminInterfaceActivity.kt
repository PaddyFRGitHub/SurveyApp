package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdminInterfaceActivity : AppCompatActivity() {

    lateinit var simpleList: ListView

    var userIdd = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)
        supportActionBar?.title = ""

        userIdd = intent.getIntExtra("userId", 0)
        //val displayUser = dbHelper.getUserByID(userIdd)
        //findViewById<TextView>(R.id.welcome).text = displayUser.userName

        //val surveyList = dbHelper.getAllSurveys()
        simpleList = findViewById<ListView>(R.id.listviewItem)

        //val surveyListsAdaptor = SurveyLists(applicationContext, surveyList)

        //simpleList!!.adapter = surveyListsAdaptor

        simpleList.isClickable = true
        simpleList.setOnItemClickListener { parent, view, positon, id ->
            //val surveyTitle = surveyList[positon]

            val intent = Intent(this, SurveyResultsActivity::class.java)
            //intent.putExtra("surveyid", surveyTitle.surveyId)
            intent.putExtra("userId", userIdd)
            startActivity(intent)
        }
    }

    fun newSurveyButton(view: View) {
        val intent = Intent(this, NewSurveyActivity::class.java)
        intent.putExtra("USERID", userIdd)
        startActivity(intent)
    }

    fun logOutBtn(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}