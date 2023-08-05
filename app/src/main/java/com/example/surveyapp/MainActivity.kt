package com.example.surveyapp

import android.content.Intent
import android.icu.util.LocaleData
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.Model.DataBaseHelper
import com.example.surveyapp.Model.Database.Database
import com.example.surveyapp.Model.Database.Survey
import com.example.surveyapp.Model.Managers.AdminSurveyManager
import com.example.surveyapp.Model.Managers.UserManager
import com.google.type.DateTime
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.UUID
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    val userManager: UserManager by lazy { UserManager() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Database.createDatabase(context = applicationContext)

        MainScope().launch {
            userManager.currentUser.collect { value ->
                Log.d("TESTY", "user changed to: $value")
                value?.let {
                    if (it.isAdmin) {
                        val adminActivity =
                            Intent(this@MainActivity, AdminInterfaceActivity::class.java)
                        startActivity(adminActivity)

                    } else {
                        val studentActivity =
                            Intent(this@MainActivity, UserInterfaceActivity::class.java)
                        startActivity(studentActivity)
                    }
                }
            }
        }
        setContentView(R.layout.activity_main)
        supportActionBar?.title = ""

        MainScope().launch {
            val manager = AdminSurveyManager()
            Log.d("TESTY", "Adding Surveys")

            manager.createNewSurvey(
                "CTEC-200",
                questions = listOf("Is the course good?", "Is the course great?")
            )

            manager.createNewSurvey(
                "BMEP-200",
                questions = listOf("Is the course good?", "Is the course great?")
            )

            manager.createNewSurvey(
                "CIEP-600",
                questions = listOf("Is the course good?", "Is the course great?")
            )

            val responses = manager.getSurveysAndResponses()

            Log.d("TESTY", "Looping responses")
            responses.forEach {
                manager.publishSurvey(it.survey, startDate = Date().time / 1000, endDate = Date().time / 1000 + 24000)
                Log.d("TESTY", it.toString())
            }


        }
    }

    fun login(view: View) {
        val userName = findViewById<EditText>(R.id.text_pnumber).text.toString()
        val passWord = findViewById<EditText>(R.id.text_Password).text.toString()

        if (userName.isBlank() && passWord.isBlank()) {
            Toast.makeText(this, "Please Fill in the boxes above!", Toast.LENGTH_LONG).show()
            return
        }
        if (userName.isBlank()) {
            Toast.makeText(this, "Username is empty!", Toast.LENGTH_LONG).show()
            return
        }
        if (passWord.isBlank()) {
            Toast.makeText(this, "Password is empty!", Toast.LENGTH_LONG).show()
            return
        }

        val loggedIn = userManager.signIn(userName, passWord)

        if (!loggedIn) {
            Toast.makeText(this, "Wrong username or password!", Toast.LENGTH_LONG).show()
        }
    }

    fun register(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

}