package com.picoder.sample.coroutinespractice.playground.basic

import kotlinx.coroutines.*
import java.io.IOException

fun main14() = runBlocking {
    // CoroutineExceptionHandler can not catch exception in deferred, exception throw in runBlocking
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Handler Caught $exception")
    }
    GlobalScope.launch(handler) {
        println("Throwing exception from launch")
        throw IndexOutOfBoundsException()
        println("Unreached")
    }

    val deferred = GlobalScope.async(handler) { // handler useless here because CoroutineExceptionHandler can not catch exception in deferred, so must try catch deferred.await()
        println("Throwing exception from async")
        throw ArithmeticException()
        println("Unreached")
    }

    try {
        deferred.await()
        println("Unreached")
    } catch (e: ArithmeticException) {
        println("Caught ArithmeticException")
    }
}

fun main() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception with suppressed ${exception.suppressed.contentToString()}") // exception.suppressed will contain all exceptions
    }
    val job = GlobalScope.launch(handler) {
        launch {
            delay(100) // without delay here it will stop other coroutine because this coroutine stopped (one child stop then all sibling stopped as well use Supervision to prevent this)
            throw IOException() // catch by CoroutineExceptionHandler
        }
        launch {
            try {
                delay(Long.MAX_VALUE) // delay forever
            } finally {
                throw ArithmeticException() // this exception in finally block is suppressed
            }
        }
        launch {
            try {
                delay(Long.MAX_VALUE) // delay forever
            } finally {
                throw IndexOutOfBoundsException() // this exception in finally block is suppressed
            }
        }

        delay(Long.MAX_VALUE)
    }
    job.join()
}