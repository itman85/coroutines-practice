package com.picoder.sample.coroutinespractice.playground.exceptionhandling

import kotlinx.coroutines.*

fun main() {

    val ceh = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Caught $throwable in CoroutineExceptionHandler")
    }

    val scope = CoroutineScope(Job())

    scope.launch(ceh) {
        try {
            // in supervisorScope the exception will not propagate to top level and  other sibling coroutine can continue, so just try catch fail coroutine
            supervisorScope {
                launch {
                    println("CEH: ${coroutineContext[CoroutineExceptionHandler]}")
                    throw RuntimeException()
                }
            }
        } catch (e: Exception) {
            println("Caught $e")
        }
    }

    Thread.sleep(100)
}