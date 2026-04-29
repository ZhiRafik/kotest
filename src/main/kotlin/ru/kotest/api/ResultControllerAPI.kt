package ru.kotest.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.kotest.dto.response.*

@RequestMapping("/results")
interface ResultControllerAPI {

    // Студент
    @GetMapping("/my")
    fun getMyResults(): ResponseEntity<List<ResultResponse>>

    @GetMapping("/my/test/{testId}")
    fun getMyResultsByTest(@PathVariable testId: Long): ResponseEntity<List<ResultResponse>>

    // Преподаватель
    @GetMapping("/test/{testId}")
    fun getTestResults(@PathVariable testId: Long): ResponseEntity<List<ResultWithStudentResponse>>

    @GetMapping("/test/{testId}/analytics")
    fun getTestAnalytics(@PathVariable testId: Long): ResponseEntity<TestAnalyticsResponse>
}