package com.picoder.sample.coroutinespractice.playground.advanced

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun events(): Flow<Int> = (1..3).asFlow().onEach { delay(1000) }

fun main8() = runBlocking<Unit> {
    events()
        .onEach { event -> println("Event: $event") }
        .collect() // <--- Collecting the flow waits all element emitted then print Done
    println("Done")
}
// if we want execute parallel, no need to wait collect completed, then we can use launchIn
// launchIn run flow in a separate coroutine so it will return job
fun main() = runBlocking<Unit> {
    val job = events()
        .onEach { event -> println("Event: $event") }
        .launchIn(this) // <--- Launching the flow in a separate coroutine
    println("Done")
    // we can cancel job or do whatever with job
}