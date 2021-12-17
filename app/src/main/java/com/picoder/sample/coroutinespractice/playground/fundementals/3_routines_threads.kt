package com.picoder.sample.coroutinespractice.playground.fundementals

import kotlin.concurrent.thread

fun main() {
    println("main starts")
    threadRoutine(1, 500)
    threadRoutine(2, 300)
    Thread.sleep(1000)
    println("main ends")
}

fun threadRoutine(number: Int, delay: Long) {
    thread {
        println("Routine $number starts work ${Thread.currentThread().name}")
        Thread.sleep(delay)
        println("Routine $number has finished ${Thread.currentThread().name}")
    }
}