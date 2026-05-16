package ru.kotest.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.kotest.model.Test
import ru.kotest.model.User

@Repository
interface TestRepository : JpaRepository<Test, Long> {
    fun findByTeacher(teacher: User): List<Test>

    @Query("SELECT DISTINCT t FROM Test t LEFT JOIN FETCH t.questions WHERE t.id = :id")
    fun findByIdWithQuestions(@Param("id") id: Long): Test?
}