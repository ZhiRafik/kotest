package ru.kotest.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.web.bind.annotation.*
import ru.kotest.api.AuthAPI
import ru.kotest.dto.AuthResponse
import ru.kotest.dto.LoginRequest
import ru.kotest.dto.RegisterRequest
import ru.kotest.security.CustomUserDetails
import ru.kotest.service.UserService

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager
) : AuthAPI {

    @PostMapping("/register")
    override fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        val user = userService.register(request);
        return ResponseEntity.ok(
            AuthResponse(
                id = user.id,
                username = user.username,
                role = user.role,
                message = "User registered successfully"
            )
        )
    }

    @PostMapping("/login")
    override fun login(
        @RequestBody request: LoginRequest,
        httpRequest: HttpServletRequest
    ) : ResponseEntity<AuthResponse> {
        val authToken = UsernamePasswordAuthenticationToken(request.username, request.password)

        val authentication = authenticationManager.authenticate(authToken)

        SecurityContextHolder.getContext().authentication = authentication

        val session = httpRequest.getSession(true)
        session.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext()
        )

        val principal = authentication.principal as CustomUserDetails

        return ResponseEntity.ok(
            AuthResponse(
                id = principal.id,
                username = principal.username,
                role = principal.role,
                message = "Login successful"
            )
        )
    }

    @GetMapping("/me")
    override fun getCurrentUser(authentication: Authentication): ResponseEntity<AuthResponse> {
        val principal = authentication.principal as CustomUserDetails
        return ResponseEntity.ok(
            AuthResponse(
                id = principal.id,
                username = principal.username,
                role = principal.role,
                message = "Current user"
            )
        )
    }

    @PostMapping("/logout")
    override fun logout(request: HttpServletRequest): ResponseEntity<Map<String, String>> {
        request.session?.invalidate()
        SecurityContextHolder.clearContext()
        return ResponseEntity.ok(mapOf("message" to "Logged out"))
    }
}