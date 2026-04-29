package ru.kotest.dto.request

data class CreateMultipleChoiceRequest(
    val text: String,
    val points: Int,
    val options: List<String>,
    val correctAnswers: Set<String>
)