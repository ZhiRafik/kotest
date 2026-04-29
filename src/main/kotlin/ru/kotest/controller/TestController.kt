package ru.kotest.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import ru.kotest.api.TestControllerAPI
import ru.kotest.dto.request.*
import ru.kotest.dto.response.*
import ru.kotest.security.CustomUserDetails
import ru.kotest.service.TestService

@RestController
class TestController(
    private val testService: TestService
) : TestControllerAPI {

    private fun getCurrentUser(): ru.kotest.model.User {
        val principal = SecurityContextHolder.getContext().authentication.principal as CustomUserDetails
        return principal.user
    }

    @PreAuthorize("hasRole('TEACHER')")
    override fun createTest(request: CreateTestRequest): ResponseEntity<TestResponse> {
        val teacher = getCurrentUser()
        val response = testService.createTest(request, teacher)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('TEACHER')")
    override fun getMyTests(): ResponseEntity<List<TestResponse>> {
        val teacher = getCurrentUser()
        val response = testService.getMyTests(teacher)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    override fun getTest(testId: Long): ResponseEntity<TestResponse> {
        val currentUser = getCurrentUser()
        val response = testService.getTest(testId, currentUser)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('TEACHER')")
    override fun updateTest(testId: Long, request: CreateTestRequest): ResponseEntity<TestResponse> {
        val teacher = getCurrentUser()
        val response = testService.updateTest(testId, request, teacher)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('TEACHER')")
    override fun deleteTest(testId: Long): ResponseEntity<Unit> {
        val teacher = getCurrentUser()
        testService.deleteTest(testId, teacher)
        return ResponseEntity.ok().build()
    }

    @PreAuthorize("hasRole('TEACHER')")
    override fun addSingleChoiceQuestion(
        testId: Long,
        request: CreateSingleChoiceRequest
    ): ResponseEntity<SingleChoiceResponse> {
        val teacher = getCurrentUser()
        val response = testService.addSingleChoiceQuestion(testId, request, teacher)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('TEACHER')")
    override fun addMultipleChoiceQuestion(
        testId: Long,
        request: CreateMultipleChoiceRequest
    ): ResponseEntity<MultipleChoiceResponse> {
        val teacher = getCurrentUser()
        val response = testService.addMultipleChoiceQuestion(testId, request, teacher)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('TEACHER')")
    override fun addTextQuestion(
        testId: Long,
        request: CreateTextQuestionRequest
    ): ResponseEntity<TextQuestionResponse> {
        val teacher = getCurrentUser()
        val response = testService.addTextQuestion(testId, request, teacher)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('TEACHER')")
    override fun updateSingleChoiceQuestion(
        questionId: Long,
        request: CreateSingleChoiceRequest
    ): ResponseEntity<SingleChoiceResponse> {
        val teacher = getCurrentUser()
        val response = testService.updateSingleChoiceQuestion(questionId, request, teacher)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('TEACHER')")
    override fun updateMultipleChoiceQuestion(
        questionId: Long,
        request: CreateMultipleChoiceRequest
    ): ResponseEntity<MultipleChoiceResponse> {
        val teacher = getCurrentUser()
        val response = testService.updateMultipleChoiceQuestion(questionId, request, teacher)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('TEACHER')")
    override fun updateTextQuestion(
        questionId: Long,
        request: CreateTextQuestionRequest
    ): ResponseEntity<TextQuestionResponse> {
        val teacher = getCurrentUser()
        val response = testService.updateTextQuestion(questionId, request, teacher)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('TEACHER')")
    override fun deleteQuestion(questionId: Long): ResponseEntity<Unit> {
        val teacher = getCurrentUser()
        testService.deleteQuestion(questionId, teacher)
        return ResponseEntity.ok().build()
    }
}