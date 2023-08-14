package com.example.surveyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.surveyapp.Model.DataBaseHelper

class MainActivity : AppCompatActivity() {

    val dbHelper: DataBaseHelper = DataBaseHelper(this)
    private val ADMINCODE = "99"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = ""
    }

    fun login(view: View) {


        val intentMain = Intent(this, UserInterfaceActivity::class.java)
        val userName = findViewById<EditText>(R.id.text_pnumber).text.toString()
        val passWord = findViewById<EditText>(R.id.text_Password).text.toString()
        val studentUser = dbHelper.getUser(userName)

        if (userName.isBlank() && passWord.isBlank()) {
            Toast.makeText(this, "Please Fill in all the boxes!", Toast.LENGTH_LONG).show()
            return
        }

        if (userName.isBlank()) {
            Toast.makeText(this, "Username is required!", Toast.LENGTH_LONG).show()
            return
        }

        if (passWord.isBlank()) {
            Toast.makeText(this, "Password is required!", Toast.LENGTH_LONG).show()
            return
        }

        if (userName == studentUser.userName && passWord != studentUser.passWord) {
            Toast.makeText(this, "Incorrect username or password!", Toast.LENGTH_LONG).show()
            return
        }

        if (userName == studentUser.userName && passWord == studentUser.passWord) {
            if (studentUser.isAdmin == 0) {
                intentMain.putExtra("userId", studentUser.userId)
                startActivity(intentMain)
            } else {
                Toast.makeText(this, "Please use the admin portal", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show()
        }
    }


    fun register(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun portal(view: View) {
        val adminCodeEditText = EditText(this)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Admin Portal Access")
            .setMessage("Please enter the admin code to proceed:")
            .setView(adminCodeEditText)
            .setPositiveButton("Enter") { _, _ ->
                val enteredCode = adminCodeEditText.text.toString()
                if (enteredCode == ADMINCODE) {

                    val intent = Intent(this, AdminPortalActivity::class.java)
                    startActivity(intent)
                } else {

                    Toast.makeText(this, "Incorrect admin code!", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

}