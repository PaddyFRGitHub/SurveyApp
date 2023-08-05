package com.example.surveyapp.Model.Managers

import android.util.Log
import com.example.surveyapp.Model.Database.Database
import com.example.surveyapp.Model.Database.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserManager {
    private val userDao = Database.db.userDAO()

    val currentUser = MutableStateFlow<User?>(null)

    fun register(
        id: String,
        password: String,
        isAdmin: Boolean
    ) {
        GlobalScope.launch {
            val user = User(
                id = id,
                password = password,
                isAdmin = isAdmin
            )

            Log.d("TESTY", "add user")
            userDao.addUser(user)

            Log.d("TESTY", "Update value to $user")
            currentUser.update { user }
        }
    }

    fun signIn(username: String, password: String): Boolean = runBlocking {
        val user = userDao.getUser(username)

        if (user != null) {
            currentUser.update { user }
            return@runBlocking user.password == password

        } else {
            return@runBlocking false
        }
    }
}