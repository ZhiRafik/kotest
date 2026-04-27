package ru.kotest.model

import jakarta.persistence.*;
import java.time.LocalDateTime

@Entity
@Table(name = "attempts")
data class Attempt(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    val student: User,

    @ManyToOne
    val test: Test,

    val startedAt: LocalDateTime,

    val finishedAt: LocalDateTime?,

    val submitted: Boolean = false
)