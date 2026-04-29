package ru.kotest.dto.response

import ru.kotest.model.UserRole

data class AuthResponse(
    val id: Long,
    val username: String,
    val role: UserRole,
    val message: String
)