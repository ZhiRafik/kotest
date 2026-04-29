package ru.kotest.mapper

import org.springframework.stereotype.Component
import ru.kotest.dto.response.AttemptDetailResponse
import ru.kotest.model.Attempt
import ru.kotest.model.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class AttemptMapper(
    private val testMapper: TestMapper
) {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    fun toAttemptDetailResponse(
        attempt: Attempt,
        test: Test,
        remainingSeconds: Int,
        shuffledQuestions: List<Any>
    ): AttemptDetailResponse {
        return AttemptDetailResponse(
            attemptId = attempt.id,
            testId = test.id,
            testTitle = test.title,
            startedAt = attempt.startedAt.format(formatter),
            expiresAt = attempt.startedAt.plusMinutes(test.durationMinutes.toLong()).format(formatter),
            remainingSeconds = remainingSeconds,
            questions = shuffledQuestions
        )
    }
}