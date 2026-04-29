package ru.kotest.dto.request

data class RegisterRequest (
    val username: String,
    val password: String,
    val role: String? = "STUDENT" // по умолчанию Student
)