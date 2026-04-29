package ru.kotest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KoTestApp

fun main(args: Array<String>) {
    runApplication<KoTestApp>(*args)
}