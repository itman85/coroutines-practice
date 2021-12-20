package com.picoder.sample.coroutinespractice.playground.coroutinebuilders

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {

    val startTime = System.currentTimeMillis()

    // non-blocking
    val deferred1 = async(start = CoroutineStart.LAZY) {
        val result1 = networkCall(1).also {
            println("result received: $it after ${elapsedMillis(startTime)}ms")
        }
        result1
    }

    // non-blocking
    val deferred2 = async {
        val result2 = networkCall(2)
        println("result received: $result2 after ${elapsedMillis(startTime)}ms")
        result2
    }
    // start LAZY above
    deferred1.start() // maybe no need here, just call await is enough
    // await() is suspend function will block at here
    val resultList = listOf(deferred1.await(), deferred2.await())

    println("Result list: $resultList after ${elapsedMillis(startTime)}ms")
}

suspend fun networkCall(number: Int): String {
    delay(500)
    return "Result $number"
}

fun elapsedMillis(startTime: Long) = System.currentTimeMillis() - startTime
