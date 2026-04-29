package ru.kotest.dto.response

data class TestAnalyticsResponse(
    val testId: Long,
    val testTitle: String,
    val totalAttempts: Int,
    val averageScore: Double,
    val averagePercentage: Double,
    val highestScore: Int,
    val lowestScore: Int,
    val scoreDistribution: Map<String, Int>  // "90-100": 5, "80-89": 3, ...
)