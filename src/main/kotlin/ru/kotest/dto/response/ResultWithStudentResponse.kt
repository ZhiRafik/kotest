package ru.kotest.dto.response

data class ResultWithStudentResponse(
    val resultId: Long,
    val studentId: Long,
    val studentName: String,
    val score: Int,
    val maxScore: Int,
    val percentage: Double,
    val submittedAt: String,
    val startedAt: String
)