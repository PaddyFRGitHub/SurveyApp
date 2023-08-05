package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.Model.DataBaseHelper
import com.example.surveyapp.Model.Managers.UserManager
import com.example.surveyapp.Model.User
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private val userManager: UserManager by lazy { UserManager() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.title = ""


        MainScope().launch {
            userManager.currentUser.collect { value ->
                Log.d("TESTY", "user changed to: $value")
                value?.let {
                    if (it.isAdmin) {

                        val adminActivity = Intent(this@RegisterActivity, AdminInterfaceActivity::class.java)
                        startActivity(adminActivity)

                    } else {
                        val studentActivity = Intent(this@RegisterActivity, UserInterfaceActivity::class.java)
                        startActivity(studentActivity)
                    }
                }
            }
        }

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

        userManager.register(username, password, isAdmin)

        Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show()
        findViewById<EditText>(R.id.text_PNumberRegister).text.clear()
        findViewById<EditText>(R.id.text_Password1).text.clear()
        if (isAdmin) {
            findViewById<EditText>(R.id.adminCode).text.clear()
        }
    }

    fun goBackToLogin(view: View) {
        finish()
    }

}