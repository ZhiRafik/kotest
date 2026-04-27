package ru.kotest.model

import jakarta.persistence.*

@Entity
data class MultipleChoiceQuestion(
    override val id: Long = 0,

    override val text: String,

    override val points: Int,

    @ManyToOne
    override val test: Test,

    @ElementCollection
    @OrderColumn
    val options: List<String>,

    @ElementCollection
    val correctAnswers: Set<String>
) : Question(id, text, points, test)