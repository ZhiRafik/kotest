package ru.kotest.mapper

import org.springframework.stereotype.Component
import ru.kotest.dto.response.*
import ru.kotest.model.*

@Component
class TestMapper {

    fun toResponse(test: Test, includeQuestions: Boolean = true): TestResponse {
        return TestResponse(
            id = test.id,
            title = test.title,
            description = test.description,
            durationMinutes = test.durationMinutes,
            teacherId = test.teacher.id,
            questionsCount = test.questions.size,
            questions = if (includeQuestions) {
                test.questions.map { toQuestionResponse(it) }
            } else {
                emptyList()
            }
        )
    }

    fun toQuestionResponse(question: Question): Any {
        return when (question) {
            is SingleChoiceQuestion -> SingleChoiceResponse(
                id = question.id,
                text = question.text,
                points = question.points,
                options = question.options
            )
            is MultipleChoiceQuestion -> MultipleChoiceResponse(
                id = question.id,
                text = question.text,
                points = question.points,
                options = question.options
            )
            is TextQuestion -> TextQuestionResponse(
                id = question.id,
                text = question.text,
                points = question.points
            )
            else -> throw IllegalArgumentException("Unknown question type")
        }
    }

    fun toSingleChoiceResponse(question: SingleChoiceQuestion): SingleChoiceResponse {
        return SingleChoiceResponse(
            id = question.id,
            text = question.text,
            points = question.points,
            options = question.options
        )
    }

    fun toMultipleChoiceResponse(question: MultipleChoiceQuestion): MultipleChoiceResponse {
        return MultipleChoiceResponse(
            id = question.id,
            text = question.text,
            points = question.points,
            options = question.options
        )
    }

    fun toTextResponse(question: TextQuestion): TextQuestionResponse {
        return TextQuestionResponse(
            id = question.id,
            text = question.text,
            points = question.points
        )
    }
}