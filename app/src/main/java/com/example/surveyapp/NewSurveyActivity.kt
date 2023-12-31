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


class NewSurveyActivity : AppCompatActivity() {

    val dbHelper = DataBaseHelper(this)
    lateinit var startDate: TextView
    lateinit var endDate: TextView
    var userNo2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_survey)
        supportActionBar?.title = ""

        var userId = intent.getIntExtra("USERID", 0)
        userNo2 = userId

        startDate = findViewById<TextView>(R.id.text_startDate)
        endDate = findViewById<TextView>(R.id.text_endDate)

        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        val currentDate = Calendar.getInstance()

        startDate.text = sdf.format(currentDate.time)
        endDate.text = sdf.format(currentDate.time)

        val newStartDate = Calendar.getInstance()
        val newEndDate = Calendar.getInstance()

        val startDateSetListener =
            OnDateSetListener { _, year, month, dayOfMonth ->
                newStartDate.set(Calendar.YEAR, year)
                newStartDate.set(Calendar.MONTH, month)
                newStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val selectedDate: String = sdf.format(newStartDate.time)
                if (newEndDate < newStartDate) {
                    Toast.makeText(
                        this,
                        "End date cannot be sooner than the start date",
                        Toast.LENGTH_SHORT
                    ).show()
                    endDate.text = ""
                }
                startDate.text = selectedDate
            }
        startDate.setOnClickListener {
            val dialog = DatePickerDialog(
                this,
                startDateSetListener,
                newStartDate.get(Calendar.YEAR),
                newStartDate.get(Calendar.MONTH),
                newStartDate.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.minDate = currentDate.timeInMillis - 1000
            dialog.show()
        }

        val endDateSetListener =
            OnDateSetListener { _, year, month, dayOfMonth ->
                newEndDate.set(Calendar.YEAR, year)
                newEndDate.set(Calendar.MONTH, month)
                newEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val selectedDate: String = sdf.format(newEndDate.time)
                endDate.text = selectedDate
            }

        endDate.setOnClickListener {
            val dialog = DatePickerDialog(
                this,
                endDateSetListener,
                newEndDate.get(Calendar.YEAR),
                newEndDate.get(Calendar.MONTH),
                newEndDate.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.minDate = newStartDate.timeInMillis - 1000
            dialog.show()
        }
    }


    fun next(view: View) {
        val title = findViewById<EditText>(R.id.text_title).text.toString()
        val startDate = startDate.text.toString()
        val endDate = endDate.text.toString()
        val checkTitle = dbHelper.getSurvey(title)

        when {
            title.isBlank() -> {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
                return
            }
            startDate.isBlank() -> {
                Toast.makeText(this, "Please add a start date", Toast.LENGTH_SHORT).show()
                return
            }
            endDate.isBlank() -> {
                Toast.makeText(this, "Please add an end date", Toast.LENGTH_SHORT).show()
                return
            }
            checkTitle.surveyTitle == title -> {
                Toast.makeText(this, "Survey already exists with this title, please change it", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("title", title)
                intent.putExtra("startDate", startDate)
                intent.putExtra("endDate", endDate)
                intent.putExtra("userId", userNo2)
                startActivity(intent)
            }
        }
    }

    fun goBack(view: View) {
        finish()
    }

}