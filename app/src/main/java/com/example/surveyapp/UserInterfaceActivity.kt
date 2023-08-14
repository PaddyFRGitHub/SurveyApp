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

    private lateinit var simpleList: ListView
    private val dbHelper: DataBaseHelper = DataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_interface)
        supportActionBar?.title = ""

        val userId = intent.getIntExtra("userId", 0)

        val displayUser = dbHelper.getUserByID(userId)
        val usernameTextView = findViewById<TextView>(R.id.textView9)
        usernameTextView.text = displayUser.userName.uppercase()

        val surveyList = dbHelper.getAllSurveys()
        simpleList = findViewById<ListView>(R.id.listviewItem2)

        val surveyListsAdapter = SurveyLists(applicationContext, surveyList)
        simpleList.adapter = surveyListsAdapter

        simpleList.isClickable = true
        simpleList.setOnItemClickListener { parent, view, position, id ->
            val surveyTitle = surveyList[position]
            val surveyId = surveyTitle.surveyId

            if (dbHelper.isSurveyCompletedByUser(userId, surveyId)) {
                Toast.makeText(this, "You have already completed this survey.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, UserAnswerActivity::class.java).apply {
                    putExtra("surveyId", surveyId)
                    putExtra("userId", userId)
                }
                startActivity(intent)
            }
        }
    }

    fun logOutBtn(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}