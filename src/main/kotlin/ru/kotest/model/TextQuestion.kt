package ru.kotest.model

import jakarta.persistence.*

@Entity
@DiscriminatorValue("TEXT")
class TextQuestion(
    id: Long = 0,
    text: String,
    points: Int,
    test: Test,

    var correctAnswer: String
) : Question(id, text, points, test) {

    override fun toString(): String {
        return "TextQuestion(id=$id, text='$text', points=$points)"
    }
}
