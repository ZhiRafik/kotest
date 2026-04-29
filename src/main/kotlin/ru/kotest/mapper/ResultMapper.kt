package ru.kotest.mapper

import org.springframework.stereotype.Component
import ru.kotest.dto.response.ResultResponse
import ru.kotest.dto.response.ResultWithStudentResponse
import ru.kotest.model.Result
import java.time.format.DateTimeFormatter

@Component
class ResultMapper {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    fun toResponse(result: Result): ResultResponse {
        return ResultResponse(
            id = result.id,
            testId = result.attempt.test.id,
            testTitle = result.attempt.test.title,
            score = result.score,
            maxScore = result.maxScore,
            percentage = result.percentage,
            submittedAt = result.attempt.finishedAt?.format(formatter) ?: ""
        )
    }

    fun toResultWithStudentResponse(result: Result): ResultWithStudentResponse {
        return ResultWithStudentResponse(
            resultId = result.id,
            studentId = result.attempt.student.id,
            studentName = result.attempt.student.username,
            score = result.score,
            maxScore = result.maxScore,
            percentage = result.percentage,
            submittedAt = result.attempt.finishedAt?.format(formatter) ?: "",
            startedAt = result.attempt.startedAt.format(formatter)
        )
    }
}