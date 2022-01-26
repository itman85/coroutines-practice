package com.picoder.sample.coroutinespractice.playground.basic

import kotlinx.coroutines.*

fun main17() = runBlocking {
    val supervisorJob = SupervisorJob()

    with(CoroutineScope(coroutineContext + supervisorJob)) {
        // launch the first child -- its exception is ignored for this example (don't do this in practice!)
        val firstChild = launch(CoroutineExceptionHandler { _, _ ->  }) {
            println("First child is failing")
            throw AssertionError("First child is cancelled")
        }
        // launch the second child
        val secondChild = launch {
            firstChild.join()
            // Cancellation of the first child is not propagated to the second child
            println("First child is cancelled: ${firstChild.isCancelled}, but second one is still active")
            try {
                delay(Long.MAX_VALUE)
            } finally {
                // But cancellation of the supervisor is propagated
                println("Second child is cancelled because supervisor is cancelled")
            }
        }
        // wait until the first child fails & completes
        firstChild.join()
        println("Cancelling supervisor")
        supervisorJob.cancel()
        secondChild.join()
    }
}

fun main() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }
    // with supervisorScope, every child coroutine throw exception will be catch by handler (if launch with context = handler)
    supervisorScope {
        val first = launch(handler) {
            println("Child 1 throws an exception")
            throw AssertionError()
        }

        val second = launch {
            delay(1000)
            println("Child 2 done")
        }

        val third = launch(handler) {
            println("Child 3 throws an exception")
            throw ArithmeticException()
        }
    }
    println("Scope is completed") // supervisorScope: parent completed when all children completed, one of children stopped will not affect other children
}