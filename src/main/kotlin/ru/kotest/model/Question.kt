package ru.kotest.model

import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Question(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0,

    open val text: String,

    open val points: Int,

    @ManyToOne
    @JoinColumn(name = "test_id")
    open val test: Test
)