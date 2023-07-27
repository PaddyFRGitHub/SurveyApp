package com.example.surveyapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.Model.DataBaseHelper
import com.example.surveyapp.Model.User

class RegisterPanel : AppCompatActivity() {

    val dbHelper: DataBaseHelper = DataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_panel)
        supportActionBar?.title = ""

        val toggle = findViewById<Switch>(R.id.btn_SwitchLogin)

        toggle.text = "User"
        findViewById<EditText>(R.id.accesCode).visibility = View.INVISIBLE

        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                toggle.text = "Admin"
                findViewById<EditText>(R.id.accesCode).visibility = View.VISIBLE
            } else {
                toggle.text = "User"
                findViewById<EditText>(R.id.accesCode).visibility = View.INVISIBLE
            }
        }
    }

    fun register(view: View) {
        val switch = findViewById<Switch>(R.id.btn_SwitchLogin)

        val username = findViewById<EditText>(R.id.text_UserNameRegister).text.toString()
        val password = findViewById<EditText>(R.id.text_Password1).text.toString()
        val password2 = findViewById<EditText>(R.id.text_Password2).text.toString()
        val accescode = findViewById<EditText>(R.id.accesCode).text.toString()

        if (switch.text == "Admin") {
            if (username.isBlank() && password.isBlank() && password2.isBlank() && accescode.isBlank()) {
                Toast.makeText(this, "Please fill in the boxes", Toast.LENGTH_SHORT).show()
                return
            }
            if (username.isBlank()) {
                Toast.makeText(this, "Please enter user name", Toast.LENGTH_SHORT).show()
                return
            }
            if (password.isBlank()) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                return
            }
            if (password2.isBlank()) {
                Toast.makeText(this, "Please re-enter password", Toast.LENGTH_SHORT).show()
                return
            }
            if (accescode.isBlank()) {
                Toast.makeText(this, "Please enter the admin code", Toast.LENGTH_SHORT).show()
                return
            }
            if (password != password2) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return
            }
            if (accescode.toInt() != 99) {
                Toast.makeText(this, "You have entered the wrong access code", Toast.LENGTH_SHORT)
                    .show()
                return
            }

            if (password == password2 && accescode.toInt() == 99) {

                val checkuser = dbHelper.getUser(username).userName
                val checkpassword = dbHelper.getUser(username).passWord

                if (checkuser == username && checkpassword == password2 ||
                    checkuser == username && checkpassword != password2
                ) {
                    Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
                } else {

                    val user = User(0, username, password2, 1)

                    if (dbHelper.addUser(user)) {
                        Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show()
                        findViewById<EditText>(R.id.text_UserNameRegister).text.clear()
                        findViewById<EditText>(R.id.text_Password1).text.clear()
                        findViewById<EditText>(R.id.text_Password2).text.clear()
                        findViewById<EditText>(R.id.accesCode).text.clear()
                    } else {
                        Toast.makeText(this, "Error: The user not added", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        if (switch.text == "User") {
            if (username.isBlank() && password.isBlank() && password2.isBlank()) {
                Toast.makeText(this, "Please fill in the boxes", Toast.LENGTH_SHORT).show()
                return
            }
            if (username.isBlank()) {
                Toast.makeText(this, "Please enter user name", Toast.LENGTH_SHORT).show()
                return
            }
            if (password.isBlank()) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                return
            }
            if (password2.isBlank()) {
                Toast.makeText(this, "Please re-enter password", Toast.LENGTH_SHORT).show()
                return
            }
            if (password != password2) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return
            }
            if (password == password2) {

                val checkuser = dbHelper.getUser(username).userName
                val checkpassword = dbHelper.getUser(username).passWord

                if (checkuser == username && checkpassword == password2 ||
                    checkuser == username && checkpassword != password2
                ) {
                    Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
                } else {
                    val user = User(0, username, password2, 0)

                    if (dbHelper.addUser(user)) {
                        Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show()
                        findViewById<EditText>(R.id.text_UserNameRegister).text.clear()
                        findViewById<EditText>(R.id.text_Password1).text.clear()
                        findViewById<EditText>(R.id.text_Password2).text.clear()
                    } else {
                        Toast.makeText(this, "Error: The user not added", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    fun goBackToLogin(view: View) {
        finish()
    }

    override fun onBackPressed() {
        return
    }
}
