package ru.kotest.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kotest.dto.request.*
import ru.kotest.dto.response.*
import ru.kotest.mapper.TestMapper
import ru.kotest.model.*
import ru.kotest.repository.QuestionRepository
import ru.kotest.repository.TestRepository

@Service
class TestService(
    private val testRepository: TestRepository,
    private val questionRepository: QuestionRepository,
    private val testMapper: TestMapper
) {

    @Transactional
    fun createTest(request: CreateTestRequest, teacher: User): TestResponse {
        val test = Test(
            title = request.title,
            description = request.description,
            durationMinutes = request.durationMinutes,
            teacher = teacher
        )
        val savedTest = testRepository.save(test)
        return testMapper.toResponse(savedTest, includeQuestions = false)
    }

    fun getMyTests(teacher: User): List<TestResponse> {
        val tests = testRepository.findByTeacher(teacher)
        return tests.map { testMapper.toResponse(it, includeQuestions = true) }
    }

    fun getTest(testId: Long, currentUser: User): TestResponse {
        val test = testRepository.findById(testId)
            .orElseThrow { IllegalArgumentException("Test not found") }

        if (currentUser.role == UserRole.TEACHER && test.teacher.id != currentUser.id) {
            throw SecurityException("You don't have permission to view this test")
        }

        return testMapper.toResponse(test, includeQuestions = true)
    }

    @Transactional
    fun updateTest(testId: Long, request: CreateTestRequest, teacher: User): TestResponse {
        val test = testRepository.findById(testId)
            .orElseThrow { IllegalArgumentException("Test not found") }

        if (test.teacher.id != teacher.id) {
            throw SecurityException("You don't have permission to update this test")
        }

        val updatedTest = test.copy(
            title = request.title,
            description = request.description,
            durationMinutes = request.durationMinutes
        )
        val savedTest = testRepository.save(updatedTest)
        return testMapper.toResponse(savedTest, includeQuestions = true)
    }

    @Transactional
    fun deleteTest(testId: Long, teacher: User) {
        val test = testRepository.findById(testId)
            .orElseThrow { IllegalArgumentException("Test not found") }

        if (test.teacher.id != teacher.id) {
            throw SecurityException("You don't have permission to delete this test")
        }

        testRepository.delete(test)
    }

    @Transactional
    fun addSingleChoiceQuestion(testId: Long, request: CreateSingleChoiceRequest, teacher: User): SingleChoiceResponse {
        val test = getTestAndCheckOwner(testId, teacher)

        val question = SingleChoiceQuestion(
            text = request.text,
            points = request.points,
            test = test,
            options = request.options,
            correctAnswer = request.correctAnswer
        )
        val savedQuestion = questionRepository.save(question)
        return testMapper.toSingleChoiceResponse(savedQuestion)
    }

    @Transactional
    fun addMultipleChoiceQuestion(testId: Long, request: CreateMultipleChoiceRequest, teacher: User): MultipleChoiceResponse {
        val test = getTestAndCheckOwner(testId, teacher)

        val question = MultipleChoiceQuestion(
            text = request.text,
            points = request.points,
            test = test,
            options = request.options,
            correctAnswers = request.correctAnswers
        )
        val savedQuestion = questionRepository.save(question)
        return testMapper.toMultipleChoiceResponse(savedQuestion)
    }

    @Transactional
    fun addTextQuestion(testId: Long, request: CreateTextQuestionRequest, teacher: User): TextQuestionResponse {
        val test = getTestAndCheckOwner(testId, teacher)

        val question = TextQuestion(
            text = request.text,
            points = request.points,
            test = test,
            correctAnswer = request.correctAnswer
        )
        val savedQuestion = questionRepository.save(question)
        return testMapper.toTextResponse(savedQuestion)
    }

    @Transactional
    fun updateSingleChoiceQuestion(questionId: Long, request: CreateSingleChoiceRequest, teacher: User): SingleChoiceResponse {
        val question = questionRepository.findById(questionId)
            .orElseThrow { IllegalArgumentException("Question not found") } as SingleChoiceQuestion

        if (question.test.teacher.id != teacher.id) {
            throw SecurityException("You don't have permission to update this question")
        }

        // Напрямую меняем свойства существующего объекта
        question.text = request.text
        question.points = request.points
        question.options = request.options
        question.correctAnswer = request.correctAnswer

        // В Hibernate измененный объект автоматически сохранится
        // при закрытии транзакции (@Transactional), но для явности вызываем save:
        val savedQuestion = questionRepository.save(question)
        return testMapper.toSingleChoiceResponse(savedQuestion)
    }

    @Transactional
    fun updateMultipleChoiceQuestion(questionId: Long, request: CreateMultipleChoiceRequest, teacher: User): MultipleChoiceResponse {
        val question = questionRepository.findById(questionId)
            .orElseThrow { IllegalArgumentException("Question not found") } as MultipleChoiceQuestion

        if (question.test.teacher.id != teacher.id) {
            throw SecurityException("You don't have permission to update this question")
        }

        // Напрямую изменяем поля существующего объекта question
        question.text = request.text
        question.points = request.points
        question.options = request.options
        question.correctAnswers = request.correctAnswers

        // Сохраняем измененный объект
        val savedQuestion = questionRepository.save(question)
        return testMapper.toMultipleChoiceResponse(savedQuestion)
    }

    @Transactional
    fun updateTextQuestion(questionId: Long, request: CreateTextQuestionRequest, teacher: User): TextQuestionResponse {
        val question = questionRepository.findById(questionId)
            .orElseThrow { IllegalArgumentException("Question not found") } as TextQuestion

        if (question.test.teacher.id != teacher.id) {
            throw SecurityException("You don't have permission to update this question")
        }

        // Напрямую изменяем поля существующего объекта question
        question.text = request.text
        question.points = request.points
        question.correctAnswer = request.correctAnswer

        // Сохраняем измененный объект
        val savedQuestion = questionRepository.save(question)
        return testMapper.toTextResponse(savedQuestion)

    }

    @Transactional
    fun deleteQuestion(questionId: Long, teacher: User) {
        val question = questionRepository.findById(questionId)
            .orElseThrow { IllegalArgumentException("Question not found") }

        if (question.test.teacher.id != teacher.id) {
            throw SecurityException("You don't have permission to delete this question")
        }

        questionRepository.delete(question)
    }

    private fun getTestAndCheckOwner(testId: Long, teacher: User): Test {
        val test = testRepository.findById(testId)
            .orElseThrow { IllegalArgumentException("Test not found") }

        if (test.teacher.id != teacher.id) {
            throw SecurityException("You don't have permission to modify this test")
        }
        return test
    }
}