package com.picoder.sample.coroutinespractice.playground.structuredconcurrency

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {

    println("Job of GlobalScope: ${GlobalScope.coroutineContext[Job]}")

    val job = GlobalScope.launch {
        delay(100)
    }

    println("Job of GlobalScope: $job")

}