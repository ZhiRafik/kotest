package ru.kotest.dto.request

data class CreateSingleChoiceRequest(
    val text: String,
    val points: Int,
    val options: List<String>,
    val correctAnswer: String
)