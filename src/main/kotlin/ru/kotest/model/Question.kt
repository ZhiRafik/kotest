package ru.kotest.model

import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // или ваша стратегия
@DiscriminatorColumn(name = "question_type")
abstract class Question(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0,

    @Column(nullable = false)
    open var text: String,

    open var points: Int,

    @ManyToOne
    open val test: Test
)
