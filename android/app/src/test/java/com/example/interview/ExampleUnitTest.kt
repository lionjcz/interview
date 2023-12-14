package com.example.interview

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testReturnText() {
        val exampleClass = ExampleClass()
        val expectedText = "Hello World"
        val actualText = exampleClass.returnText()
        assertEquals(expectedText, actualText)
    }
}

class ExampleClass {
    fun returnText(): String {
        return "Hello World"
    }
}