package com.picoder.sample.coroutinespractice.playground.cancellation

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {

    val job = launch(Dispatchers.Default) {
        repeat(10) { index ->
            if (isActive) {
                println("operation number $index")
                Thread.sleep(100) // this is not suspend function, so it will not throw CancellationException as jon call cancel
                // that why it needs to check isActive of this coroutine
            } else {
                // NonCancellable make sure will run coroutine and not cancelled it although job is cancelled
                // so we can continue to clean up processing on cancellation
                withContext(NonCancellable) {
                    // perform some cleanup on cancellation
                    delay(100)
                    println("Clean up done!")
                }
                throw CancellationException()
            }
        }
    }

    delay(250)
    println("Cancelling Coroutine")
    job.cancel()


}