package com.example.surveyapp.Model.Managers

import com.example.surveyapp.Model.DataBaseHelper
import com.example.surveyapp.Model.Database.Answer
import com.example.surveyapp.Model.Database.AppDatabase
import com.example.surveyapp.Model.Database.DAO.QuestionDAO
import com.example.surveyapp.Model.Database.Database
import com.example.surveyapp.Model.Database.Question
import com.example.surveyapp.Model.Database.Response
import com.example.surveyapp.Model.Database.Survey
import com.example.surveyapp.Model.Questions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Date
import java.util.UUID

class AdminSurveyManager {
    private val questionDAO = Database.db.questionDAO()
    private val surveyDAO = Database.db.surveyDAO()
    private val responsesDAO = Database.db.responsesDAO()
    private val answerDAO = Database.db.answerDAO()

    // Helper Functions

    // Adds a new Survey to the database with a list of questions
    fun createNewSurvey(title: String, questions: List<String>) {
        val surveyId = UUID.randomUUID().toString()

        val survey = Survey(
            id = surveyId,
            title = title,
            startDate = null,
            endDate = null
        )

        val questions = questions.map {
            Question(
                id = UUID.randomUUID().toString(),
                surveyId = surveyId,
                questionText = it
            )
        }

        GlobalScope.launch {
            surveyDAO.addSurvey(survey)
            questions.forEach { questionDAO.addQuestion(it) }
        }
    }

    fun publishSurvey(
        survey: Survey,
        startDate: Long,
        endDate: Long
    ) {
        GlobalScope.launch {
            val updated = survey.copy(
                startDate = startDate,
                endDate = endDate
            )

            surveyDAO.updateSurvey(updated)
        }
    }

    fun getSurveysAndResponses(): List<SurveyResponse> = runBlocking {
        val surveys = surveyDAO.getAllSurveys()

        return@runBlocking surveys.map { survey ->
            val responses = responsesDAO.getResponsesForSurvey(survey.id)

            val questionsAndAnswers = responses.map {
                ResponseContainer(
                    id = it.id,
                    question = questionDAO.getQuestion(it.questionId),
                    answer = answerDAO.getAnswer(it.answerId)
                )
            }

            return@map SurveyResponse(
                survey = survey,
                responses = questionsAndAnswers,
                expired = survey.endDate?.let {
                    Date().time > it
                } ?: false,
                published = survey.endDate != null
            )
        }
    }

    data class SurveyResponse(
        val survey: Survey,
        val responses: List<ResponseContainer>,
        val expired: Boolean,
        val published: Boolean
    )

    data class ResponseContainer(
        val id: String,
        val question: Question,
        val answer: Answer
    )
}