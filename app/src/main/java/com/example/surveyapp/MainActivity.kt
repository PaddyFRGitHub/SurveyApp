package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.Model.DataBaseHelper

class MainActivity : AppCompatActivity() {

    val dbHelper: DataBaseHelper = DataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = ""
    }

    fun login(view: View) {

        val intent = Intent(this, AdminPanel::class.java)
        val intent2 = Intent(this, UserPanel::class.java)

        val userName = findViewById<EditText>(R.id.text_username).text.toString()
        val passWord = findViewById<EditText>(R.id.text_Password).text.toString()

        val actualUsername = dbHelper.getUser(userName)

        if (userName.isBlank() && passWord.isBlank()) {
            Toast.makeText(this, "Please Fill in the boxes above", Toast.LENGTH_LONG).show()
            return
        }
        if (userName.isBlank()) {
            Toast.makeText(this, "Please Fill in username", Toast.LENGTH_LONG).show()
            return
        }
        if (passWord.isBlank()) {
            Toast.makeText(this, "Please Fill in password", Toast.LENGTH_LONG).show()
            return
        }

        if (userName == actualUsername.userName && passWord != actualUsername.passWord) {
            Toast.makeText(this, "password is wrong", Toast.LENGTH_LONG).show()
            return
        }
        if (userName == actualUsername.userName && passWord == actualUsername.passWord && actualUsername.isAdmin == 0) {
            intent2.putExtra("userId", actualUsername.userId)
            startActivity(intent2)
            return
        }
        if (userName == actualUsername.userName && passWord == actualUsername.passWord && actualUsername.isAdmin == 1) {
            intent.putExtra("userId", actualUsername.userId)
            startActivity(intent)
            return
        } else {
            Toast.makeText(this, "User does not exist ", Toast.LENGTH_LONG).show()
        }
    }


    fun register(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}