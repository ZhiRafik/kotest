package ru.kotest.dto.response

data class SingleChoiceResponse(
    val id: Long,
    val text: String,
    val points: Int,
    val options: List<String>
)