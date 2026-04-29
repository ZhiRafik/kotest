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

    fun findByStudent(student: User): List<Attempt>
    fun findByStudentAndSubmitted(student: User, submitted: Boolean): List<Attempt>
    fun findByStudentAndTest(student: User, test: Test): List<Attempt>

    fun findByStudentAndTestAndSubmittedFalse(student: User, test: Test): Attempt?

    // По тесту
    fun findByTest(test: Test): List<Attempt>
    fun findByTestAndSubmitted(test: Test, submitted: Boolean): List<Attempt>

    fun findBySubmittedFalseAndStartedAtBefore(startedAt: java.time.LocalDateTime): List<Attempt>

    fun countByTest(test: Test): Long

    @Query("SELECT AVG(EXTRACT(EPOCH FROM (a.finishedAt - a.startedAt)) / 60) FROM Attempt a WHERE a.test = :test AND a.submitted = true")
    fun findAverageCompletionTimeMinutes(@Param("test") test: Test): Double?
}