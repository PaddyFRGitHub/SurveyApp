package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.Model.DataBaseHelper
import com.example.surveyapp.Model.User

class RegisterActivity : AppCompatActivity() {

    private val dbHelper: DataBaseHelper = DataBaseHelper(this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.title = ""

        val toggle = findViewById<CheckBox>(R.id.btn_ChangeUser)
        toggle.text = "Student"
        findViewById<EditText>(R.id.adminCode).visibility = View.INVISIBLE

        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                toggle.text = "Admin"
                findViewById<EditText>(R.id.adminCode).visibility = View.VISIBLE
            } else {
                toggle.text = "Student"
                findViewById<EditText>(R.id.adminCode).visibility = View.INVISIBLE
            }
        }
    }

    fun register(view: View) {
        val checkBox = findViewById<CheckBox>(R.id.btn_ChangeUser)
        val username = findViewById<EditText>(R.id.text_PNumberRegister).text.toString()
        val password = findViewById<EditText>(R.id.text_Password1).text.toString()
        val code = findViewById<EditText>(R.id.adminCode).text.toString()

        if (username.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val isAdmin = checkBox.text == "Admin"

        if (isAdmin && code != "99") {
            Toast.makeText(this, "Incorrect admin code", Toast.LENGTH_SHORT).show()
            return
        }
        if (dbHelper.getUser(username) == null) {
            Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
            return
        }

        val user = User(0, username, password, if (isAdmin) 1 else 0)

        if (dbHelper.addUser(user)) {
            Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show()
            findViewById<EditText>(R.id.text_PNumberRegister).text.clear()
            findViewById<EditText>(R.id.text_Password1).text.clear()
            if (isAdmin) {
                findViewById<EditText>(R.id.adminCode).text.clear()
            }
        } else {
            Toast.makeText(this, "Error: User not added", Toast.LENGTH_SHORT).show()
        }
    }

    fun homeButton(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}