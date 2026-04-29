package ru.kotest.dto.response

data class AttemptDetailResponse(
    val attemptId: Long,
    val testId: Long,
    val testTitle: String,
    val startedAt: String,
    val expiresAt: String,
    val remainingSeconds: Int,
    val questions: List<Any>  // SingleChoiceResponse | MultipleChoiceResponse | TextResponse
)