package com.picoder.sample.coroutinespractice.playground.structuredconcurrency

import kotlinx.coroutines.*

fun main() {

    val exceptionHandler = CoroutineExceptionHandler { context, exception ->
        println("Caught exception $exception")
    }

    val scope = CoroutineScope(SupervisorJob() + exceptionHandler) // use Job() will cancel scope and it's children
    // use SupervisorJob() will not propagate failed coroutine and others will continue to complete
    //  this is different bwt Job and SupervisorJob

    scope.launch {
        println("Coroutine 1 starts")
        delay(50)
        println("Coroutine 1 fails")
        throw RuntimeException()
    }

    scope.launch {
        println("Coroutine 2 starts")
        delay(500)
        println("Coroutine 2 completed")
    }.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("Coroutine 2 got cancelled!")
        }
    }

    Thread.sleep(1000)

    println("Scope got cancelled: ${!scope.isActive}")

    // coroutine 1 fail then it cancel sibling coroutine and propagate to cancel parent
    // but use SupervisorJob() it will not propagate fail coroutine to parent and its sibling coroutine continue to complete

}