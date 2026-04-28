package ru.kotest.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import ru.kotest.dto.AuthResponse
import ru.kotest.dto.LoginRequest
import ru.kotest.dto.RegisterRequest

@Tag(name = "Authentication", description = "Регистрация, логин, выход из системы")
@RequestMapping("/auth")
interface AuthAPI {

    @Operation(summary = "Регистрация нового пользователя")
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse>

    @Operation(summary = "Вход в систему")
    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
        httpRequest: HttpServletRequest
    ): ResponseEntity<AuthResponse>

    @Operation(summary = "Получить текущего авторизованного пользователя")
    @GetMapping("/me")
    fun getCurrentUser(authentication: Authentication): ResponseEntity<AuthResponse>

    @Operation(summary = "Выход из системы")
    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<Map<String, String>>
}