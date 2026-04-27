package ru.kotest.model

import jakarta.persistence.*;

@Entity
@Table(name = "results")
data class Result(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne
    val attempt: Attempt,

    val score: Int,

    val maxScore: Int,

    val percentage: Double
)