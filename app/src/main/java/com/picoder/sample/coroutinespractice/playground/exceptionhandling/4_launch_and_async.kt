package com.picoder.sample.coroutinespractice.playground.exceptionhandling

import kotlinx.coroutines.*

fun main() {

    val exceptionHandler = CoroutineExceptionHandler { context, exception ->
        println("Caught $exception in CoroutineExceptionHandler")
    }

    val scope = CoroutineScope(Job() + exceptionHandler)

    scope.async {
        println("Coroutine Start")
        launch {
            val deferred = async {
                println("Coroutine Starting...")
                delay(200)
                throw RuntimeException() // this exception will encapsulated in deferred result return from async
                // it will throw when call await or run in launch (launch will handle exception propagate to top level)
                println("Coroutine End")
            }
        }
        println("Coroutine ...")
    }

    Thread.sleep(1000)

}