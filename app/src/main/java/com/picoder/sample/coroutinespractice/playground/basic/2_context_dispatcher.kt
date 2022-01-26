package com.picoder.sample.coroutinespractice.playground.basic

import kotlinx.coroutines.*

@ObsoleteCoroutinesApi
fun main() {
    // newSingleThreadContext is ObsoleteCoroutinesApi
    newSingleThreadContext("thread1").use { ctx1 ->
        // create context is ctx1, not launch coroutine yet
        // ctx1 has 1 element is dispatcher which decide this coroutine run in thread name thread1
        println("ctx1 - ${Thread.currentThread().name}") // main thread

        newSingleThreadContext("thread2").use { ctx2 ->
            // create context is ctx2, not launch coroutine yet
            // ctx2 has 1 element is dispatcher which decide this coroutine run in thread name thread2
            println("ctx2 - ${Thread.currentThread().name}") // main thread

            // run coroutine with context ctx1
            runBlocking(ctx1) {
                // coroutine running on context ctx1 and thread thread1
                println("Started in ctx1 - ${Thread.currentThread().name}")

                // use withContext to switch context from ctx1 to ctx2
                withContext(ctx2) {
                    // coroutine running on context ctx2 and thread thread2
                    println("Working in ctx2 - ${Thread.currentThread().name}")
                }

                // coroutine exit block withContext so it will back run on context ctx1 and thread thread1
                println("Back to ctx1 - ${Thread.currentThread().name}")
            }
        }

        println("out of ctx2 block - ${Thread.currentThread().name}") // main thread
    }

    println("out of ctx1 block - ${Thread.currentThread().name}") // main thread
}

fun main1() = runBlocking {
    launch(Dispatchers.Unconfined) { // not confined yet so it will run on main thread
        println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
        delay(1000)
        // when hit suspend function, it will run on bg thread
        println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
    }
    println("...")
}