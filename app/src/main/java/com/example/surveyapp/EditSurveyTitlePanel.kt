package com.example.surveyapp

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.Model.DataBaseHelper
import java.text.SimpleDateFormat
import java.util.*

class EditSurveyTitlePanel : AppCompatActivity() {
    val dbHelper: DataBaseHelper = DataBaseHelper(this)

    var transferId = 0
    lateinit var startDate2: TextView
    lateinit var endDate2: TextView
    var userIdddd = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_survey_title_panel)
        supportActionBar?.title = ""

        val id = intent.getIntExtra("surveyId", 0)
        userIdddd = intent.getIntExtra("userId", 0)
        val chosenIdSurvey = dbHelper.getSurveyById(id)

        startDate2 = findViewById(R.id.text_startDate2)
        endDate2 = findViewById(R.id.text_endDate2)

        val myCalendar = Calendar.getInstance()
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        val newStartDate = Calendar.getInstance()
        val newEndDate = Calendar.getInstance()

        transferId = chosenIdSurvey.surveyId
        findViewById<EditText>(R.id.text_title2).setText(chosenIdSurvey.surveyTitle)
        startDate2.text = chosenIdSurvey.surveyStartDate
        endDate2.text = chosenIdSurvey.surveyEndDate

        val startDate2SetListener =
            OnDateSetListener { _, year, month, dayOfMonth ->
                newStartDate.set(Calendar.YEAR, year)
                newStartDate.set(Calendar.MONTH, month)
                newStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val selectedDate: String = sdf.format(newStartDate.time)
                if (newEndDate < newStartDate) {
                    Toast.makeText(
                        this,
                        "End date can't be eariler then start date",
                        Toast.LENGTH_SHORT
                    ).show()
                    endDate2.text = ""
                }
                startDate2.text = selectedDate
            }
        startDate2.setOnClickListener {
            val dialog = DatePickerDialog(
                this,
                startDate2SetListener,
                newStartDate.get(Calendar.YEAR),
                newStartDate.get(Calendar.MONTH),
                newStartDate.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.minDate = myCalendar.timeInMillis - 1000
            dialog.show()
        }

        val endDate2SetListener =
            OnDateSetListener { _, year, month, dayOfMonth ->
                newEndDate.set(Calendar.YEAR, year)
                newEndDate.set(Calendar.MONTH, month)
                newEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val selectedDate: String = sdf.format(newEndDate.time)
                endDate2.text = selectedDate
            }

        endDate2.setOnClickListener {
            val dialog = DatePickerDialog(
                this,
                endDate2SetListener,
                newEndDate.get(Calendar.YEAR),
                newEndDate.get(Calendar.MONTH),
                newEndDate.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.minDate = newStartDate.timeInMillis - 1000
            dialog.show()
        }

    }

    fun next2(view: View) {
        val intent = Intent(this, EditSurveyQuestions::class.java)

        val title = findViewById<EditText>(R.id.text_title2).text.toString()
        val startDate = findViewById<TextView>(R.id.text_startDate2).text.toString()
        val endDate = findViewById<TextView>(R.id.text_endDate2).text.toString()


        if (title.isBlank()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            return
        }
        if (startDate.isBlank()) {
            Toast.makeText(this, "Please enter a starting date", Toast.LENGTH_SHORT).show()
            return
        }
        intent.putExtra("surveyId", transferId)
        intent.putExtra("userId", userIdddd)
        intent.putExtra("title", title)
        intent.putExtra("startDate", startDate)
        intent.putExtra("endDate", endDate)

        startActivity(intent)

    }

    fun goBack2(view: View) {
        finish()
    }

    override fun onBackPressed() {
        return
    }
}