package com.picoder.sample.coroutinespractice.playground.exceptionhandling

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun main(){
    val scope = CoroutineScope(Job())

    scope.launch {
        //functionThtThrows()
        try {
            functionThtThrows()
        }catch (e:Exception){
            println("Caught $e")
        }

    }

    Thread.sleep(1000)
    println("end")
}

private fun functionThtThrows(){
    throw RuntimeException()
}