package ru.kotest.service

import org.slf4j.LoggerFactory
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

    private val log = LoggerFactory.getLogger(javaClass)
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    fun getAvailableTests(): List<TestResponse> {
        log.debug("Fetching available tests")
        val tests = testRepository.findAll()
        return tests.map { test ->
            testMapper.toResponse(test, includeQuestions = false)
        }
    }

    @Transactional
    fun startAttempt(testId: Long, student: User): AttemptDetailResponse {
        log.info("=== START ATTEMPT ===")
        log.info("testId={}, studentId={}", testId, student.id)

        val test = testRepository.findByIdWithQuestions(testId)
            ?: throw IllegalArgumentException("Test not found")

        log.info("Test loaded: id={}, title={}", test.id, test.title)
        log.info("Test.questions size BEFORE access: {}", test.questions.size)  //  это может упасть
        log.info("Ne ypal")

        val existingAttempt = attemptRepository.findByStudentAndTestAndSubmittedFalse(student, test)
        if (existingAttempt != null) {
            log.info("Existing attempt found, returning it")
            return buildAttemptDetailResponse(existingAttempt, test)
        }

        log.info("No existing attempt, creating new one")
        val startedAt = LocalDateTime.now()
        val attempt = Attempt(
            student = student,
            test = test,
            startedAt = startedAt,
            finishedAt = null,
            submitted = false
        )

        val savedAttempt = attemptRepository.save(attempt)
        log.info("New attempt created: id={}", savedAttempt.id)
        return buildAttemptDetailResponse(savedAttempt, test)
    }

    private fun buildAttemptDetailResponse(attempt: Attempt, test: Test): AttemptDetailResponse {
        log.debug("Building attempt detail response for attemptId={}, testId={}", attempt.id, test.id)

        val now = LocalDateTime.now()
        val expiresAt = attempt.startedAt.plusMinutes(test.durationMinutes.toLong())
        val remainingSeconds = if (now.isBefore(expiresAt)) {
            java.time.Duration.between(now, expiresAt).seconds.toInt()
        } else {
            0
        }

        // Перемешиваем вопросы и варианты ответов (случайная генерация)
        val shuffledQuestions = test.questions.shuffled().mapNotNull { question ->
            log.debug("Processing question id={}, type={}, text='{}'",
                question.id, question::class.simpleName, question.text)

            when (question) {
                is SingleChoiceQuestion -> {
                    log.debug("SingleChoice: id={}, text='{}', options={}, correctAnswer={}",
                        question.id, question.text, question.options, question.correctAnswer)
                    testMapper.toSingleChoiceResponse(question).copy(
                        options = question.options.shuffled()
                    )
                }
                is MultipleChoiceQuestion -> {
                    log.debug("MultipleChoice: id={}, text='{}', options={}, correctAnswers={}",
                        question.id, question.text, question.options, question.correctAnswers)
                    testMapper.toMultipleChoiceResponse(question).copy(
                        options = question.options.shuffled()
                    )
                }
                is TextQuestion -> {
                    log.debug("Text: id={}, text='{}', correctAnswer={}",
                        question.id, question.text, question.correctAnswer)
                    testMapper.toTextResponse(question)
                }
                else -> {
                    log.error("Unknown question type: {}", question::class.simpleName)
                    throw IllegalArgumentException("Unknown question type")
                }
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
        log.info("Submitting attemptId={} for studentId={}", attemptId, student.id)

        val attempt = attemptRepository.findById(attemptId)
            .orElseThrow { IllegalArgumentException("Attempt not found") }

        // Проверка: попытка принадлежит студенту
        if (attempt.student.id != student.id) {
            log.warn("StudentId={} tried to submit attemptId={} belonging to studentId={}",
                student.id, attemptId, attempt.student.id)
            throw SecurityException("Cannot submit another student's attempt")
        }

        // Проверка: попытка уже не отправлена
        if (attempt.submitted) {
            log.warn("AttemptId={} already submitted by studentId={}", attemptId, student.id)
            throw IllegalStateException("Attempt already submitted")
        }

        // Проверка таймера
        val test = attempt.test
        val expiresAt = attempt.startedAt.plusMinutes(test.durationMinutes.toLong())
        val now = LocalDateTime.now()

        if (now.isAfter(expiresAt)) {
            log.warn("AttemptId={} submitted after time limit. Expired at {}, now={}", attemptId, expiresAt, now)
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
                log.debug("QuestionId={} answered correctly (+{} points)", question.id, question.points)
            } else {
                log.debug("QuestionId={} answered incorrectly", question.id)
            }
        }

        val percentage = if (maxScore > 0) (totalScore.toDouble() / maxScore) * 100 else 0.0
        log.info("AttemptId={} scored {} / {} ({}%)", attemptId, totalScore, maxScore, percentage)

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