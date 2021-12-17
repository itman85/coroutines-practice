package com.picoder.sample.coroutinespractice.playground.fundementals

import kotlinx.coroutines.*

fun main() = runBlocking {
    println("main starts")
    joinAll(
        async { threadSwitchingCoroutine(1, 500) },
        async { threadSwitchingCoroutine(2, 300) }
    )
    println("main ends")
}

suspend fun threadSwitchingCoroutine(number: Int, delay: Long) {
    println("Coroutine $number starts work on ${Thread.currentThread().name}")
    delay(delay)
    withContext(Dispatchers.IO) {
        println("Coroutine $number has finished on ${Thread.currentThread().name}")
    }
}