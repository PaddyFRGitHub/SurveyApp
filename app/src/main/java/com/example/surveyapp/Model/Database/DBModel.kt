package com.example.surveyapp.Model.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Survey(
    @PrimaryKey val id: String,
    val title: String,
    val startDate: Long?,
    val endDate: Long?
)

@Entity
data class Question(
    @PrimaryKey val id: String,
    val surveyId: String,
    val questionText: String
)

@Entity
data class Answer(
    @PrimaryKey val id: String,
    val answer: String
)

@Entity
data class User(
    @PrimaryKey val id: String,
    val isAdmin: Boolean,
    val password: String
)

@Entity
data class Response(
    @PrimaryKey val id: String,
    val studentId: String,
    val surveyId: String,
    val questionId: String,
    val answerId: String
)