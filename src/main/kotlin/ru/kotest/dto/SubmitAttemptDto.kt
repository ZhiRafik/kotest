package ru.kotest.dto

data class SubmitAttemptDto(
    val attemptId: Long,
    val answers: List<AnswerDto>
)