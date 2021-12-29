package com.picoder.sample.coroutinespractice.playground.cancellation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val job = launch {
        repeat(10) { index ->
            println("operation number $index")
            try {
                delay(100)
            } catch (exception: CancellationException) {
                println("CancellationException was thrown")
                throw CancellationException() // this will exit this block, if not throw it will continue to repeat
            }
        }

        // above delay in try catch will continue repeat although job already cancelled
        // but this delay will throw CancellationException, and not print 'additional operation number 12'
        println("additional operation number 11")
        delay(100)

        println("additional operation number 12")
        delay(100)
    }

    delay(250)
    println("Cancelling Coroutine")
    job.cancel()
}