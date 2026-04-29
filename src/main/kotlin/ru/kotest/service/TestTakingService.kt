package ru.kotest.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kotest.dto.SubmitAttemptDto
import ru.kotest.dto.response.*
import ru.kotest.mapper.AttemptMapper
import ru.kotest.mapper.TestMapper
import ru.kotest.model.*
import ru.kotest.repository.AttemptRepository
import ru.kotest.repository.QuestionRepository
import ru.kotest.repository.ResultRepository
import ru.kotest.repository.TestRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class TestTakingService(
    private val testRepository: TestRepository,
    private val attemptRepository: AttemptRepository,
    private val questionRepository: QuestionRepository,
    private val resultRepository: ResultRepository,
    private val testMapper: TestMapper,
    private val attemptMapper: AttemptMapper
) {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    fun getAvailableTests(): List<TestResponse> {
        val tests = testRepository.findAll()
        return tests.map { test ->
            testMapper.toResponse(test, includeQuestions = false)
        }
    }

    @Transactional
    fun startAttempt(testId: Long, student: User): AttemptDetailResponse {
        val test = testRepository.findById(testId)
            .orElseThrow { IllegalArgumentException("Test not found") }

        // Проверяем, нет ли уже незавершенной попытки
        val existingAttempt = attemptRepository.findByStudentAndTestAndSubmittedFalse(student, test)
        if (existingAttempt != null) {
            return buildAttemptDetailResponse(existingAttempt, test)
        }

        val startedAt = LocalDateTime.now()

        val attempt = Attempt(
            student = student,
            test = test,
            startedAt = startedAt,
            finishedAt = null,
            submitted = false
        )

        val savedAttempt = attemptRepository.save(attempt)
        return buildAttemptDetailResponse(savedAttempt, test)
    }

    private fun buildAttemptDetailResponse(attempt: Attempt, test: Test): AttemptDetailResponse {
        val now = LocalDateTime.now()
        val expiresAt = attempt.startedAt.plusMinutes(test.durationMinutes.toLong())
        val remainingSeconds = if (now.isBefore(expiresAt)) {
            java.time.Duration.between(now, expiresAt).seconds.toInt()
        } else {
            0
        }

        // Перемешиваем вопросы и варианты ответов (случайная генерация)
        val shuffledQuestions = test.questions.shuffled().map { question ->
            when (question) {
                is SingleChoiceQuestion -> testMapper.toSingleChoiceResponse(question).copy(
                    options = question.options.shuffled()
                )
                is MultipleChoiceQuestion -> testMapper.toMultipleChoiceResponse(question).copy(
                    options = question.options.shuffled()
                )
                is TextQuestion -> testMapper.toTextResponse(question)
                else -> throw IllegalArgumentException("Unknown question type")
            }
        }

        return attemptMapper.toAttemptDetailResponse(
            attempt = attempt,
            test = test,
            remainingSeconds = remainingSeconds,
            shuffledQuestions = shuffledQuestions
        )
    }

    @Transactional
    fun submitAttempt(attemptId: Long, request: SubmitAttemptDto, student: User): ResultResponse {
        val attempt = attemptRepository.findById(attemptId)
            .orElseThrow { IllegalArgumentException("Attempt not found") }

        // Проверка: попытка принадлежит студенту
        if (attempt.student.id != student.id) {
            throw SecurityException("Cannot submit another student's attempt")
        }

        // Проверка: попытка уже не отправлена
        if (attempt.submitted) {
            throw IllegalStateException("Attempt already submitted")
        }

        // Проверка таймера
        val test = attempt.test
        val expiresAt = attempt.startedAt.plusMinutes(test.durationMinutes.toLong())
        val now = LocalDateTime.now()

        if (now.isAfter(expiresAt)) {
            throw IllegalStateException("Time limit exceeded")
        }

        // Оценка ответов
        var totalScore = 0
        var maxScore = 0

        for (answer in request.answers) {
            val question = questionRepository.findById(answer.questionId)
                .orElseThrow { IllegalArgumentException("Question not found: ${answer.questionId}") }

            maxScore += question.points

            val isCorrect = when (question) {
                is SingleChoiceQuestion -> {
                    val selected = answer.selectedAnswers?.firstOrNull()
                    selected == question.correctAnswer
                }
                is MultipleChoiceQuestion -> {
                    val selected = answer.selectedAnswers?.toSet() ?: emptySet()
                    selected == question.correctAnswers
                }
                is TextQuestion -> {
                    val text = answer.textAnswer?.trim()?.lowercase()
                    val correct = question.correctAnswer.trim().lowercase()
                    text == correct
                }
                else -> false
            }

            if (isCorrect) {
                totalScore += question.points
            }
        }

        val percentage = if (maxScore > 0) (totalScore.toDouble() / maxScore) * 100 else 0.0

        // Сохраняем результат
        val result = Result(
            attempt = attempt,
            score = totalScore,
            maxScore = maxScore,
            percentage = percentage
        )

        // Отмечаем попытку как завершенную
        val updatedAttempt = attempt.copy(
            submitted = true,
            finishedAt = now
        )

        attemptRepository.save(updatedAttempt)
        val savedResult = resultRepository.save(result)

        return ResultResponse(
            id = savedResult.id,
            testId = test.id,
            testTitle = test.title,
            score = totalScore,
            maxScore = maxScore,
            percentage = percentage,
            submittedAt = now.format(formatter)
        )
    }
}