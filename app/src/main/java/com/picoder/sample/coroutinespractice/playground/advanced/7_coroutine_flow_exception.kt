package com.picoder.sample.coroutinespractice.playground.advanced

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

// emit source
fun foo7(): Flow<Int> = flow {
    for (i in 3 downTo -3) {
        println("3 / $i = ${3 / i}") // where exception happened
        emit(i) // emit next value
    }
}

fun foo71(): Flow<Int> = flow {
    for (i in 3 downTo -3) {
        emit(i) // emit next value
    }
}

fun main70() = runBlocking {
    // try catch will catch exception from emit source and collect source
    try {
        foo7().collect { value ->
            println("VALUE = $value")
        }
    }catch (e:Throwable){
        println("Caught $e")
    }
}



fun main7() = runBlocking {
    try {
        foo71().collect { value ->
            println("3 / $value = ${3 / value}") // where exception happened in collect source
        }
    } catch (e: Throwable) {
        println("Caught $e")
    }
}

// catch() operator will catch exception from emit source,  but cannot catch exception from collect source
// flow.catch() help emit element into flow
fun main71() = runBlocking {
    foo72().catch { e -> emit("Caught ${e.message}") } // emit element for exception case
        .collect { value -> println("VALUE = $value")
        }
}

fun foo72(): Flow<String> = flow {
    for (i in 3 downTo -3) {
        println("3 / $i = ${3 / i}") // exception when i = 0
        emit(i.toString()) // emit next value
    }
}

fun foo73(): Flow<Int> = flow {
    for (i in 3 downTo -3) {
        emit(i) // emit next value
    }
}
//  to catch in collect, use onEach()
fun main() = runBlocking {
    foo73().onEach { value ->
        println("3 / $value = ${3 / value}") // where Exception
    }.catch { e -> println("Caught $e") }
        .collect()
}