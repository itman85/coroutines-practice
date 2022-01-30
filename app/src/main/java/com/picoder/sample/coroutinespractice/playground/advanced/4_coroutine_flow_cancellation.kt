package com.picoder.sample.coroutinespractice.playground.advanced

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

fun foo4(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(2000) // suspend because delay
        println("Emitting $i")
        emit(i)
    }
}

fun foo41(): Flow<Int> = flow {
    for (i in 1..3) {
        Thread.sleep(2000) // not suspend
        println("Emitting $i")
        emit(i)
    }
}
fun main() = runBlocking {
    // timeout only work when there is something suspend inside flow
    withTimeoutOrNull(5000) { // Timeout after 5s
        foo41().collect { value -> println(value) } // it still be cancel after thread sleep emit 3
        foo4().collect { value -> println(value) } // it is cancelled after delay emit 2
    }
    println("Done")
}