package ru.kotest.dto

data class QuestionDto(
    val id: Long,
    val text: String,
    val points: Int,
    val type: QuestionType,
    val options: List<String>? = null
)