package ru.kotest.dto.request

data class CreateTestRequest(
    val title: String,
    val description: String,
    val durationMinutes: Int
)