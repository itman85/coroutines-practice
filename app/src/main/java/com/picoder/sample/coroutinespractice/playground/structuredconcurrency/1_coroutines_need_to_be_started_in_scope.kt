package com.picoder.sample.coroutinespractice.playground.structuredconcurrency

import kotlinx.coroutines.*

val scope = CoroutineScope(Dispatchers.Default)

fun main() = runBlocking<Unit> {

    val job = scope.launch {
        println("Coroutine started")
        delay(100)
        println("Coroutine completed")
    }

    job.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("Coroutine was cancelled")
        }
    }

    delay(50)
    onDestroy()
}

fun onDestroy() {
    println("life-time of scope ends")
    scope.cancel()
}