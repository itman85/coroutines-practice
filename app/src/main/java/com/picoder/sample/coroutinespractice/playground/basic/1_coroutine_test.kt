package com.picoder.sample.coroutinespractice.playground.basic

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(){
    // non-blocking, it will print sleeping ... while coroutine running
    /*GlobalScope.launch {
        sayHello()
    }*/
    // blocking, it will wait for coroutine completed then print sleeping ...
    runBlocking{
        sayHello()
    }
    println("sleeping ...")
    Thread.sleep(3000)
    println("Done!")
}

suspend fun sayHello() {
    println("turn on ...")
    delay(2000L)
    println("Hello!") // print hello while thread still sleeping
}