package com.picoder.sample.coroutinespractice.playground.basic

import kotlinx.coroutines.*

fun main2() = runBlocking {
    val job = launch { // launch a new coroutine and keep a reference to its Job
        delay(5000L)
        println("World!")
    }
    println("Hello,")
    job.join() // block at here and wait until child coroutine completes
    println("Kotlin")
}

fun main3() = runBlocking {
    val job = launch {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L) // delay check coroutine isActive itself, if false this suspend function will completed
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancel() // cancels the job, it will set isActive = false
    println("main: Now I can quit.")
}

// Coroutine cancellation is cooperative
fun main4() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) { // while (isActive) will finished immediately as job cancel
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancel() // cancels the job, but job still run until block code is completed because Coroutine cancellation is cooperative
    println("main: Now I can quit.")
}

fun main() = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            // job cancel but it still run finally block, we can use this to release resources
            /*
            println("I'm running finally")
        delay(1000L) // as coroutine cancelled, delay will not run because inside this function check isActive = false
        println("Print me please!")
            */

            withContext(NonCancellable) {  // NonCancellable will help this block run despite coroutine already cancelled
                println("I'm running finally")
                delay(1000L)
                println("I'm non-cancellable")
            }
        }
    }
    delay(1300L) // delay a bit
    println("main: I'm tired of waiting!")
    job.cancel() // cancels the job
    println("main: Now I can quit.")
}

fun main5() = runBlocking {
    // it will throw TimeoutCancellationException if timeout but task not completed
    withTimeout(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    // can use withTimeoutOrNull will return null as timeout instead of exception
    val result = withTimeoutOrNull(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
        "Done" // will get cancelled before it produces this result
    }
    println("Result is $result")
}