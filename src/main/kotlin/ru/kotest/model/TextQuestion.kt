package ru.kotest.model

import jakarta.persistence.*;
@Entity
data class TextQuestion(
    override val id: Long = 0,

    override val text: String,

    override val points: Int,

    @ManyToOne
    override val test: Test,

    val correctAnswer: String
) : Question(id, text, points, test)