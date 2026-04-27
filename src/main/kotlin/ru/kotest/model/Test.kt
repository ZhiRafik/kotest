package ru.kotest.model

import jakarta.persistence.*

@Entity
@Table(name = "tests")
data class Test(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val title: String,

    val description: String,

    val durationMinutes: Int,

    @OneToMany(mappedBy = "test", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val questions: MutableList<Question> = mutableListOf(),

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    val teacher: User
)