package ru.kotest.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import ru.kotest.service.UserService

@Service
class CustomUserDetailsService(
    private val userService: UserService
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userService.findByUsername(username)
        return CustomUserDetails(user)
    }
}