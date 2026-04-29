package ru.kotest.service

import org.springframework.stereotype.Service
import ru.kotest.dto.response.*
import ru.kotest.mapper.ResultMapper
import ru.kotest.model.User
import ru.kotest.model.UserRole
import ru.kotest.repository.ResultRepository
import ru.kotest.repository.TestRepository

@Service
class ResultService(
    private val resultRepository: ResultRepository,
    private val testRepository: TestRepository,
    private val resultMapper: ResultMapper
) {

    fun getMyResults(student: User): List<ResultResponse> {
        val results = resultRepository.findByAttemptStudent(student)
        return results.map { resultMapper.toResponse(it) }
    }

    fun getMyResultsByTest(testId: Long, student: User): List<ResultResponse> {
        val test = testRepository.findById(testId)
            .orElseThrow { IllegalArgumentException("Test not found") }

        val results = resultRepository.findByAttemptStudentAndAttemptTest(student, test)
        return results.map { resultMapper.toResponse(it) }
    }

    fun getTestResults(testId: Long, teacher: User): List<ResultWithStudentResponse> {
        val test = testRepository.findById(testId)
            .orElseThrow { IllegalArgumentException("Test not found") }

        if (test.teacher.id != teacher.id) {
            throw SecurityException("You don't have permission to view results for this test")
        }

        val results = resultRepository.findByAttemptTest(test)
        return results.map { resultMapper.toResultWithStudentResponse(it) }
    }

    fun getTestAnalytics(testId: Long, teacher: User): TestAnalyticsResponse {
        val test = testRepository.findById(testId)
            .orElseThrow { IllegalArgumentException("Test not found") }

        if (test.teacher.id != teacher.id) {
            throw SecurityException("You don't have permission to view analytics for this test")
        }

        val results = resultRepository.findByAttemptTest(test)

        val totalAttempts = results.size
        val averagePercentage = resultRepository.findAveragePercentageByTest(test) ?: 0.0
        val highestScore = resultRepository.findMaxScoreByTest(test) ?: 0
        val lowestScore = resultRepository.findMinScoreByTest(test) ?: 0
        val averageScore = if (results.isNotEmpty()) results.map { it.score }.average() else 0.0

        // Распределение баллов по 10-балльным интервалам
        val distribution = mutableMapOf(
            "0-9" to 0, "10-19" to 0, "20-29" to 0, "30-39" to 0,
            "40-49" to 0, "50-59" to 0, "60-69" to 0, "70-79" to 0,
            "80-89" to 0, "90-100" to 0
        )

        results.forEach { result ->
            val percentage = result.percentage.toInt()
            val range = when {
                percentage < 10 -> "0-9"
                percentage < 20 -> "10-19"
                percentage < 30 -> "20-29"
                percentage < 40 -> "30-39"
                percentage < 50 -> "40-49"
                percentage < 60 -> "50-59"
                percentage < 70 -> "60-69"
                percentage < 80 -> "70-79"
                percentage < 90 -> "80-89"
                else -> "90-100"
            }
            distribution[range] = distribution[range]!! + 1
        }

        return TestAnalyticsResponse(
            testId = test.id,
            testTitle = test.title,
            totalAttempts = totalAttempts,
            averageScore = averageScore,
            averagePercentage = averagePercentage,
            highestScore = highestScore,
            lowestScore = lowestScore,
            scoreDistribution = distribution.filter { it.value > 0 }
        )
    }
}