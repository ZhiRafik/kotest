package ru.kotest.dto.response

data class TextQuestionResponse(
    val id: Long,
    val text: String,
    val points: Int
)