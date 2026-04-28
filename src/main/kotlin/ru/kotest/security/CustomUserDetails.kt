package ru.kotest.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.kotest.model.User
import ru.kotest.model.UserRole

class CustomUserDetails (
    val user: User
) : UserDetails {

    val id: Long get() = user.id
    val role: UserRole get() = user.role

    override fun getPassword(): String = user.password
    override fun getUsername(): String = user.username
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}