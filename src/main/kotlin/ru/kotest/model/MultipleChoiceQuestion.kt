package ru.kotest.model

import jakarta.persistence.*

@Entity
@DiscriminatorValue("MULTIPLE")
class MultipleChoiceQuestion(
    id: Long = 0,
    text: String,
    points: Int,
    test: Test,

    @ElementCollection
    @OrderColumn
    var options: List<String>,

    @ElementCollection
    var correctAnswers: Set<String>
) : Question(id, text, points, test) {

    override fun toString(): String {
        return "MultipleChoiceQuestion(id=$id, text='$text', points=$points, options=$options)"
    }
}
