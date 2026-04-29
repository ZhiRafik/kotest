package ru.kotest.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.kotest.model.Attempt
import ru.kotest.model.Result
import ru.kotest.model.Test
import ru.kotest.model.User

@Repository
interface ResultRepository : JpaRepository<Result, Long> {

    fun findByAttemptStudent(student: User): List<Result>
    fun findByAttemptStudentAndAttemptTest(student: User, test: Test): List<Result>

    fun findByAttemptTest(test: Test): List<Result>

    fun findByAttempt(attempt: Attempt): Result?

    @Query("SELECT AVG(r.percentage) FROM Result r WHERE r.attempt.test = :test")
    fun findAveragePercentageByTest(@Param("test") test: Test): Double?

    @Query("SELECT COUNT(r) FROM Result r WHERE r.attempt.test = :test")
    fun countByTest(@Param("test") test: Test): Long

    @Query("SELECT MAX(r.score) FROM Result r WHERE r.attempt.test = :test")
    fun findMaxScoreByTest(@Param("test") test: Test): Int?

    @Query("SELECT MIN(r.score) FROM Result r WHERE r.attempt.test = :test")
    fun findMinScoreByTest(@Param("test") test: Test): Int?
}