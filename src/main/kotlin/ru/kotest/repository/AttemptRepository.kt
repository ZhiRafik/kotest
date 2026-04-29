package ru.kotest.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.kotest.model.Attempt
import ru.kotest.model.Test
import ru.kotest.model.User

@Repository
interface AttemptRepository : JpaRepository<Attempt, Long> {
    fun findByStudentAndTestAndSubmittedFalse(student: User, test: Test): Attempt?
}