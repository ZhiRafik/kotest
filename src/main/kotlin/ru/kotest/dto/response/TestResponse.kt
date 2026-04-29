package ru.kotest.dto.response

data class TestResponse(
    val id: Long,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val teacherId: Long,
    val createdAt: String? = null,
    val questionsCount: Int = 0,
    val questions: List<Any>
)