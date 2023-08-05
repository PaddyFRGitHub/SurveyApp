package com.example.surveyapp.Model.Managers

import android.provider.ContactsContract.Data
import com.example.surveyapp.Model.DataBaseHelper
import com.example.surveyapp.Model.Database.Answer
import com.example.surveyapp.Model.Database.Database
import com.example.surveyapp.Model.Database.Question
import com.example.surveyapp.Model.Database.Response
import com.example.surveyapp.Model.Database.Survey
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.UUID

class UserSurveyManager {
    private val userManager = UserManager()
    private val questionDAO = Database.db.questionDAO()
    private val surveyDAO = Database.db.surveyDAO()
    private val responsesDAO = Database.db.responsesDAO()

    fun getPublishedSurveys(): List<SurveyContainer> = runBlocking {
        surveyDAO.getAllSurveys().mapNotNull { survey ->
            if (survey.startDate != null && survey.endDate != null)
                SurveyContainer(
                    survey = survey,
                    questions = questionDAO.getQuestionsForSurvey(survey.id),
                    completed = responsesDAO.getResponsesForSurvey(survey.id)
                        .map { it.studentId }
                        .contains(userManager.currentUser.value?.id ?: "")
                )
            else
                null
        }
    }

    data class SurveyContainer(
        val survey: Survey,
        val questions: List<Question>,
        val completed: Boolean
    )

    class SurveyTaker(
        private val surveyContainer: SurveyContainer
    ) {
        private val userManager = UserManager()
        private val answerDAO = Database.db.answerDAO()
        private val responsesDAO = Database.db.responsesDAO()

        private var currentQuestion: Question = surveyContainer.questions.first()
        private var responses: MutableList<Pair<Question, String>> = mutableListOf()

        fun start(): Question {
            return currentQuestion
        }

        // Submit an answer for the current question.
        // Returns the next question
        // null signifies not more questions and the end of the survey
        fun submit(answer: String): Question? {
            // Add to current responses
            responses += Pair(currentQuestion, answer)

            val currentQuestionIndex = surveyContainer.questions.indexOf(currentQuestion)
            val nextQuestion = surveyContainer.questions.getOrNull(currentQuestionIndex + 1)

            // Update and return next question
            return if (nextQuestion != null) {
                currentQuestion = nextQuestion
                currentQuestion
            } else {
                saveAnswersToDatabase()
                null
            }
        }

        private fun saveAnswersToDatabase() {
            GlobalScope.launch {
                responses.forEach {
                    val answerId = UUID.randomUUID().toString()

                    answerDAO.addAnswer(Answer(
                        id = answerId,
                        answer = it.second
                    ))

                    responsesDAO.addResponse(
                        Response(
                            id = UUID.randomUUID().toString(),
                            surveyId = surveyContainer.survey.id,
                            questionId = it.first.id,
                            answerId = answerId,
                            studentId = userManager.currentUser.value?.id ?: ""
                        )
                    )
                }
            }
        }
    }
}
