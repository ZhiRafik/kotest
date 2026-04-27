package ru.kotest.dto

data class RegisterRequest (
    val username: String,
    val password: String,
    val role: String? = "STUDENT" // по умолчанию Student
)