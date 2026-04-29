package ru.kotest.service

import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.kotest.dto.request.RegisterRequest
import ru.kotest.model.User
import ru.kotest.model.UserRole
import ru.kotest.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun register(request: RegisterRequest): User {
        if (userRepository.existsByUsername(request.username)) {
            throw IllegalArgumentException("Username '${request.username}' already exists")
        }

        val role = when (request.role?.uppercase()) {
            "TEACHER" -> UserRole.TEACHER
            else -> UserRole.STUDENT
        }

        val user = User(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            role = role
        )

        return userRepository.save(user)
    }

    fun findByUsername(username: String): User {
        return userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found: $username")
    }

    fun findById(id: Long): User {
        return userRepository.findById(id)
            .orElseThrow { IllegalArgumentException("User not found: $id") }
    }
}