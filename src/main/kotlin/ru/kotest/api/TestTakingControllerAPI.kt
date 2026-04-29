package ru.kotest.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.kotest.dto.SubmitAttemptDto
import ru.kotest.dto.response.*

@RequestMapping("/tests")
interface TestTakingControllerAPI {

    @GetMapping("/available")
    fun getAvailableTests(): ResponseEntity<List<TestResponse>>

    @PostMapping("/{testId}/start")
    fun startAttempt(@PathVariable testId: Long): ResponseEntity<AttemptDetailResponse>

    @PostMapping("/attempts/{attemptId}/submit")
    fun submitAttempt(
        @PathVariable attemptId: Long,
        @RequestBody request: SubmitAttemptDto
    ): ResponseEntity<ResultResponse>
}