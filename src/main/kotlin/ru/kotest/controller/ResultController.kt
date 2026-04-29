package ru.kotest.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import ru.kotest.api.ResultControllerAPI
import ru.kotest.dto.response.*
import ru.kotest.security.CustomUserDetails
import ru.kotest.service.ResultService

@RestController
class ResultController(
    private val resultService: ResultService
) : ResultControllerAPI {

    private fun getCurrentUser(): ru.kotest.model.User {
        val principal = SecurityContextHolder.getContext().authentication.principal as CustomUserDetails
        return principal.user
    }

    @PreAuthorize("hasRole('STUDENT')")
    override fun getMyResults(): ResponseEntity<List<ResultResponse>> {
        val student = getCurrentUser()
        val response = resultService.getMyResults(student)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('STUDENT')")
    override fun getMyResultsByTest(testId: Long): ResponseEntity<List<ResultResponse>> {
        val student = getCurrentUser()
        val response = resultService.getMyResultsByTest(testId, student)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('TEACHER')")
    override fun getTestResults(testId: Long): ResponseEntity<List<ResultWithStudentResponse>> {
        val teacher = getCurrentUser()
        val response = resultService.getTestResults(testId, teacher)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('TEACHER')")
    override fun getTestAnalytics(testId: Long): ResponseEntity<TestAnalyticsResponse> {
        val teacher = getCurrentUser()
        val response = resultService.getTestAnalytics(testId, teacher)
        return ResponseEntity.ok(response)
    }
}