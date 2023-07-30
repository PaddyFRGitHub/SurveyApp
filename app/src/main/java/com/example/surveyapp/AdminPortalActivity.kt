package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.Model.DataBaseHelper

class AdminPortalActivity : AppCompatActivity() {

    val dbHelper: DataBaseHelper = DataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_portal)
        supportActionBar?.title = ""
    }

    fun adminlogin(view: View) {

        val intent = Intent(this, AdminInterfaceActivity::class.java)
        val userName = findViewById<EditText>(R.id.text_adminuser).text.toString()
        val passWord = findViewById<EditText>(R.id.text_adminPassword).text.toString()
        val code = findViewById<EditText>(R.id.adminCode2).text.toString()
        val actualUser = dbHelper.getUser(userName)

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
        if (actualUser == null) {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show()
            return
        }
        if (passWord == actualUser.passWord) {
            if (actualUser.isAdmin == 1) {
                if (code == "99") {
                    intent.putExtra("userId", actualUser.userId)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Incorrect admin code", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show()
            }
        }
    }
}
