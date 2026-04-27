package ru.kotest.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.kotest.model.User
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
}