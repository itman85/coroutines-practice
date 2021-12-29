package com.picoder.sample.coroutinespractice.playground.exceptionhandling

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun main(){
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Caught $throwable in CoroutineException")
    }
    val scope = CoroutineScope(Job() /*+ exceptionHandler*/)

    // exception handler must set in top level coroutine
    scope.launch(exceptionHandler) {
        /*
        // it will not affected if set exceptionHandler in child coroutine
        launch(exceptionHandler) {
            functionThtThrows()
        }*/
        functionThtThrows()
    }

    Thread.sleep(100)
}

private fun functionThtThrows(){
    throw RuntimeException()
}