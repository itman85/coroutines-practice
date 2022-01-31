package com.picoder.sample.coroutinespractice.playground.advanced

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

// flow will run same context with collect source
fun foo2(): Flow<Int> = flow {
    log("Started foo flow")
    for (i in 1..3) {
        emit(i) // emit source
    }
}

// it will throw exception IllegalStateException because cannot change context by using withContext
fun foo3(): Flow<Int> = flow {
    // The WRONG way to change context for CPU-consuming code in flow builder
    kotlinx.coroutines.withContext(Dispatchers.Default) {
        for (i in 1..3) {
            Thread.sleep(100) // pretend we are computing it in CPU-consuming way
            emit(i) // emit next value
        }
    }
}

// flowOn(given context) will create another coroutine run in given context
fun foo31(): Flow<Int> = flow {
    for (i in 1..3) {
        Thread.sleep(100) // pretend we are computing it in CPU-consuming way
        log("Emitting $i")
        emit(i) // emit next value
    }
}.flowOn(Dispatchers.Default) // RIGHT way to change context for CPU-consuming code in flow builder


fun main() = runBlocking {
    foo31().collect { value -> log("Collected $value") } // collect source
}