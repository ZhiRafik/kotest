package ru.kotest.dto

data class TestCreateDto(
    val title: String,
    val description: String,
    val durationMinutes: Int
)