package com.picoder.sample.coroutinespractice.playground.exceptionhandling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() = runBlocking<Unit>() {

    try {
        doSomeThingSuspend()
    } catch (e: Exception) {
        println("Caught $e")
    }

}

private suspend fun doSomeThingSuspend() {
    // in coroutineScope the exception will propagate to top level and fail other sibling coroutine
    coroutineScope {
        launch {
            throw RuntimeException()
        }
    }
}
