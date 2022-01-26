package com.picoder.sample.coroutinespractice.playground.basic

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val time = measureTimeMillis {
        val one = async (start = CoroutineStart.LAZY){ printOne() } // it will execute printOne immediately
        val two = async (start = CoroutineStart.LAZY) { printTwo() } // it will execute printTwo immediately
        //delay(5000) // one and two already completed, then call await() will return immediately
        //one.start() // start the first one
        //two.start() // start the second one
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}

fun main11() = runBlocking {
    val time = measureTimeMillis {
        val one = async(start = CoroutineStart.LAZY) { printOne() } // LAZY will not execute yet, wait until call start()
        val two = async(start = CoroutineStart.LAZY) { printTwo() } // LAZY not execute yet, wait until call start()
        one.start() // start the first one
        two.start() // start the second one
        println("The answer is ${one.await() + two.await()}") // if forget call start(), when call await() it will run synchronously
    }
    println("Completed in $time ms")
}

suspend fun printOne(): Int {
    println("process one...")
    delay(1000L)
    println("finish one")
    return 10
}

suspend fun printTwo(): Int {
    println("process two...")
    delay(1000L)
    println("finish two")
    return 20
}

