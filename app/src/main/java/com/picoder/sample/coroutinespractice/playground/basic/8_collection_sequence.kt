package com.picoder.sample.coroutinespractice.playground.basic

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

suspend fun foo(): List<Int> {
    val list = mutableListOf<Int>()
    for (i in 1..3) {
        delay(1000)
        list.add(i)
    }

    return list
}

fun foo1(): Sequence<Int> = sequence { // sequence builder
    for (i in 1..3) {
        Thread.sleep(1000)
        yield(i)
    }
}

fun main() = runBlocking {
    val time = measureTimeMillis {
        foo1().forEach { value -> println(value) }
    }
    println(time)
}