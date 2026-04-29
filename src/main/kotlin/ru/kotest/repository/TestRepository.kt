package ru.kotest.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.kotest.model.Test
import ru.kotest.model.User

@Repository
interface TestRepository : JpaRepository<Test, Long> {
    fun findByTeacher(teacher: User): List<Test>
    fun findAllByOrderByCreatedAtDesc(): List<Test>
}