package ru.kotest.dto.request

data class LoginRequest(
    val username: String,
    val password: String
)