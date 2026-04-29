package ru.kotest.dto.response
data class ResultResponse(
    val id: Long,
    val testId: Long,
    val testTitle: String,
    val score: Int,
    val maxScore: Int,
    val percentage: Double,
    val submittedAt: String
)