package com.picoder.sample.coroutinespractice.playground.advanced

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

// we can use finally to know when flow completed,
// or we  can use onComplete operator, onCompletion will has param to check if complete with exception or not
fun foo9(): Flow<Int> = (1..3).asFlow()

// use finally to know when flow completed
fun main9() = runBlocking<Unit> {
    try {
        foo9().collect { value -> println(value) }
    } finally {
        println("Done")
    }
}

// use onCompletion to know when flow complete
fun main91() = runBlocking<Unit> {
    foo9()
        .onCompletion { println("Done") }
        .collect { value -> println(value) }
}

fun foo91(): Flow<Int> = flow {
    emit(1)
    throw RuntimeException()
}

// use onCompletion with param to check if flow completed with exception or not
// cause = null mean no exception, otherwise has exception
fun main() = runBlocking<Unit> {
    foo91()
        .onCompletion { cause -> if (cause != null) println("Flow completed exceptionally") }
        .catch { cause -> println("Caught exception") }
        .collect { value -> println(value) }
}