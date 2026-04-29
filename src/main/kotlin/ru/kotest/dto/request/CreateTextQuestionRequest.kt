package ru.kotest.dto.request
data class CreateTextQuestionRequest(
    val text: String,
    val points: Int,
    val correctAnswer: String
)