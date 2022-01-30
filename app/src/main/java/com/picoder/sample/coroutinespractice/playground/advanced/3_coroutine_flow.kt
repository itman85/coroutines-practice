package com.picoder.sample.coroutinespractice.playground.advanced

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

// Sequence block main thread, it run synchronously
fun foo(): Sequence<Int> = sequence { // sequence builder
    for (i in 1..3) {
        Thread.sleep(1000)
        yield(i) // yield next value
    }
}

// Flow non-block main thread, it run asynchronously
fun foo1(): Flow<Int> = flow {
    println("Flow started")
    // flow builder
    for (i in 1..3) {
        delay(1000)
        emit(i) // emit next value
    }
}

fun main31() = runBlocking {
    // Launch a concurrent coroutine to check if the main thread is blocked
    launch {
        println(Thread.currentThread().name)
        for (k in 1..3) {
            println("I'm blocked $k")
            delay(1000)
        }
    }
    val time = measureTimeMillis {
        //foo().forEach { value -> println(value) } // sequence use iterator and block main thread while waiting for yield next item
        foo1().collect { value -> println(value) } // flow call collect is suspend function so not block main thread while waiting for emit next item
    }
    println("$time s")
}

fun main() = runBlocking<Unit> {
    println("Calling foo...")
    val flow = foo1() // Flow is cold stream, so it will execute code in block flow {} until collect() call
    println("Calling collect...")
    flow.collect { value -> println(value) }
    println("Calling collect again...")
    flow.collect { value -> println(value) }
}

// flow{}, flowOf, .asFlow() extension function
fun main32() = runBlocking {
    val data = flowOf(1,"abc", 3.4, "def")
    data.collect { println(it) }
}
