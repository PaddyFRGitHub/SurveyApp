package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
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

        val appAdaptor = App(applicationContext, surveyList)

        simpleList!!.adapter = appAdaptor

        simpleList.isClickable = true
        simpleList.setOnItemClickListener { parent, view, positon, id ->
            val surveyTitle = surveyList[positon]


            val intent = Intent(this, ChangeUserSurveyActivity::class.java)
            intent.putExtra("surveyId", surveyTitle.surveyId)
            intent.putExtra("userId", userId)


            startActivity(intent)
        }
    }

    fun logOutBtn(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        return
    }
}