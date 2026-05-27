package ru.otus.otuskotlin.project

import kotlin.test.Test
import kotlin.test.assertEquals

class AppTest {
    @Test
    fun testGreet() {
        assertEquals("Hello, Anna!", App().greet("Anna"))
    }
}