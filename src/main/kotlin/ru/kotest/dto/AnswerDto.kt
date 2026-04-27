package ru.kotest.dto

data class AnswerDto(
    val questionId: Long,
    val selectedAnswers: List<String>?, // для single/multiple choice
    val textAnswer: String?  // для текстового вопроса
)