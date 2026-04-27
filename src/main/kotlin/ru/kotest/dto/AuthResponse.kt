package ru.kotest.dto

import ru.kotest.model.UserRole

data class AuthResponse(
    val id: Long,
    val username: String,
    val role: UserRole,
    val message: String
)