package ru.kotest.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import ru.kotest.api.TestTakingControllerAPI
import ru.kotest.dto.SubmitAttemptDto
import ru.kotest.dto.response.*
import ru.kotest.security.CustomUserDetails
import ru.kotest.service.TestTakingService

@RestController
class TestTakingController(
    private val testTakingService: TestTakingService
) : TestTakingControllerAPI {

    private fun getCurrentUser(): ru.kotest.model.User {
        val principal = SecurityContextHolder.getContext().authentication.principal as CustomUserDetails
        return principal.user
    }

    @PreAuthorize("hasRole('STUDENT')")
    override fun getAvailableTests(): ResponseEntity<List<TestResponse>> {
        val response = testTakingService.getAvailableTests()
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('STUDENT')")
    override fun startAttempt(testId: Long): ResponseEntity<AttemptDetailResponse> {
        val student = getCurrentUser()
        val response = testTakingService.startAttempt(testId, student)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('STUDENT')")
    override fun submitAttempt(
        attemptId: Long,
        request: SubmitAttemptDto
    ): ResponseEntity<ResultResponse> {
        val student = getCurrentUser()
        val response = testTakingService.submitAttempt(attemptId, request, student)
        return ResponseEntity.ok(response)
    }
}