package ru.kotest.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.kotest.model.Question
import ru.kotest.model.Test

@Repository
interface QuestionRepository : JpaRepository<Question, Long> {
    fun findByTest(test: Test): List<Question>
    fun findByTestId(testId: Long): List<Question>
    fun deleteByTestId(testId: Long)
    fun findByTestAndType(test: Test, type: String): List<Question>

}