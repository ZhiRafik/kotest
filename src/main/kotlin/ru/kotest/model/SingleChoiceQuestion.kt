package ru.kotest.model

import jakarta.persistence.*

@Entity
@DiscriminatorValue("SINGLE")
class SingleChoiceQuestion(
    id: Long = 0,
    text: String,
    points: Int,
    test: Test,

    @ElementCollection
    @OrderColumn
    var options: List<String>,

    var correctAnswer: String
) : Question(id, text, points, test) {

    // Переопределяем toString вручную, чтобы безопасно логировать
    override fun toString(): String {
        return "SingleChoiceQuestion(id=$id, text='$text', points=$points, options=$options)"
    }
}
