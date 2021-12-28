package com.picoder.sample.coroutinespractice.playground.structuredconcurrency

import kotlinx.coroutines.*

fun main() {

    val scope = CoroutineScope(Job())

    scope.launch {

        doSomeTasks() // suspend function -> blocking code

        launch {
            println("Starting Task 3")
            delay(300)
            println("Task 3 completed")
        }
    }

    Thread.sleep(1000)
}

suspend fun doSomeTasks() = coroutineScope { // todo what is purpose of coroutineScope,supervisorScope? how to use them?
    launch {
        println("Starting Task 1")
        delay(100)
        println("Task 1 completed")
    }

    launch {
        println("Starting Task 2")
        delay(200)
        println("Task 2 completed")
    }
}