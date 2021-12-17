package com.picoder.sample.coroutinespractice.playground.coroutinebuilders

import kotlinx.coroutines.*

fun main() {
    // runBlocking will run all code in block and finish first then print End main
    runBlocking<Unit> {
        val job = launch(start = CoroutineStart.LAZY) { // LAZY will start as job call start, DEFAULT will start immediately
            networkRequest()
            println("result received")
        }
        println("start of runBlocking")
        delay(800)
       // job.start() // when CoroutineStart.LAZY and not wait job finished to print end of runBlocking
        //job.join() // will wait for job finished then print end of runBlocking
        println("end of runBlocking")
    }
    // process finished will kill this global scope coroutine
    /*GlobalScope.launch {
        networkRequest()
        println("result received")
    }*/
    println("End main")
}

suspend fun networkRequest(): String {
    delay(500)
    return "Result"
}