package ru.kotest.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.kotest.model.Question
import ru.kotest.model.Test

@Repository
interface QuestionRepository : JpaRepository<Question, Long> {
}