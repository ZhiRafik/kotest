package ru.kotest.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.kotest.dto.request.*
import ru.kotest.dto.response.*

@RequestMapping("/tests")
interface TestControllerAPI {

    @PostMapping
    fun createTest(@RequestBody request: CreateTestRequest): ResponseEntity<TestResponse>

    @GetMapping
    fun getMyTests(): ResponseEntity<List<TestResponse>>

    @GetMapping("/{testId}")
    fun getTest(@PathVariable testId: Long): ResponseEntity<TestResponse>

    @PutMapping("/{testId}")
    fun updateTest(
        @PathVariable testId: Long,
        @RequestBody request: CreateTestRequest
    ): ResponseEntity<TestResponse>

    @DeleteMapping("/{testId}")
    fun deleteTest(@PathVariable testId: Long): ResponseEntity<Unit>

    @PostMapping("/{testId}/questions/single")
    fun addSingleChoiceQuestion(
        @PathVariable testId: Long,
        @RequestBody request: CreateSingleChoiceRequest
    ): ResponseEntity<SingleChoiceResponse>

    @PostMapping("/{testId}/questions/multiple")
    fun addMultipleChoiceQuestion(
        @PathVariable testId: Long,
        @RequestBody request: CreateMultipleChoiceRequest
    ): ResponseEntity<MultipleChoiceResponse>

    @PostMapping("/{testId}/questions/text")
    fun addTextQuestion(
        @PathVariable testId: Long,
        @RequestBody request: CreateTextQuestionRequest
    ): ResponseEntity<TextQuestionResponse>

    @PutMapping("/questions/single/{questionId}")
    fun updateSingleChoiceQuestion(
        @PathVariable questionId: Long,
        @RequestBody request: CreateSingleChoiceRequest
    ): ResponseEntity<SingleChoiceResponse>

    @PutMapping("/questions/multiple/{questionId}")
    fun updateMultipleChoiceQuestion(
        @PathVariable questionId: Long,
        @RequestBody request: CreateMultipleChoiceRequest
    ): ResponseEntity<MultipleChoiceResponse>

    @PutMapping("/questions/text/{questionId}")
    fun updateTextQuestion(
        @PathVariable questionId: Long,
        @RequestBody request: CreateTextQuestionRequest
    ): ResponseEntity<TextQuestionResponse>

    @DeleteMapping("/questions/{questionId}")
    fun deleteQuestion(@PathVariable questionId: Long): ResponseEntity<Unit>
}