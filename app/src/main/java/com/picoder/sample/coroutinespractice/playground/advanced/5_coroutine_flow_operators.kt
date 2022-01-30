package com.picoder.sample.coroutinespractice.playground.advanced

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun numbers(): Flow<Int> = flow {
    try {
        emit(1)
        delay(1000)
        emit(2)
        delay(1000)
        println("This line will not execute")
        emit(3)
    } catch (e: CancellationException) {
        println("exception")
    } finally {
        println("close resource here")
    }
}

// take
fun main() = runBlocking {
    numbers()
        .take(2) // take only the first two
        .collect { value -> println(value) }
}

// transform
fun main51() = runBlocking {
    (1..9).asFlow() // a flow of requests
        .transform { value ->
            if (value % 2 == 0) { // Emit only even values, but twice
                emit(value * value)
                emit(value * value * value)
            } // Do nothing if odd
        }
        .collect { response -> println(response) }
}

// map
fun main52() = runBlocking {
    (1..3).asFlow()
        .map { it * it } // squares of numbers from 1 to 5
        .collect { println(it) }
}

// filter
fun main53() = runBlocking {
    (1..5).asFlow()
        .filter {
            println("Filter $it")
            it % 2 == 0
        }.collect {
            println("Collect $it")
        }
}

// onEach
fun main54() = runBlocking {
    val nums = (1..3).asFlow().onEach { delay(3000) } // numbers 1..3 every 300 ms
    val startTime = System.currentTimeMillis()
    nums.collect { value ->
        println("$value at ${System.currentTimeMillis() - startTime} ms from start")
    }
}

// reduce return a value
fun main55() = runBlocking {
    val sum = (1..3).asFlow()
        .map { it * it } // squares of numbers from 1 to 5
        .reduce { a, b -> a + b } // sum them or do whatever action, a is accumulator value, b is new value
    println(sum)
}

// fold same to reduce but with initial value, reduce has initial value = 0
fun main56() = runBlocking {
    val sum = (1..3).asFlow()
        .fold(initial = 10) { a, b -> // initial value is 10
            println("accumulator: $a")
            println("new value: $b")
            a + b
        } // sum them (terminal operator)
    println("result = $sum")
}

// toList() convert flow to ArrayList, toSet() convert flow to LinkedHashSet
fun main57() = runBlocking {
    val list: List<String> = listOf("a", "b", "c", "d", "e").asFlow().toList()
    val set: Set<Int> = (1..5).asFlow().toSet()
    println("${list.javaClass} $list")
    println("${set.javaClass} $set")
}

// first() get first item in flow or with condition, throw NoSuchElementException if not get any thing from flow
fun main58() = runBlocking {
    val a: Int = listOf(1, 3, 5, 7, 2, 6, 8, 4).asFlow().first()
    val b: Int = listOf(1, 3, 5, 7, 2, 6, 8, 4).asFlow().first { it % 2 == 0 }
    println(a)
    println(b)
}
// single() use to check if flow has only one element, if more or less than one it will throw IllegalStateException
fun main59() = runBlocking {
    val a: Int = listOf(10).asFlow().single() // return 10
    listOf(1, 2).asFlow().single() // throw IllegalStateException because has more than 1 element
    listOf<Int>().asFlow().single() // throw IllegalStateException because has less than 1 element
    println(a) // 10
}

// singleOrNull() same to single() but return null instead of IllegalStateException if has no element, still throw Exception if has more than 1 element
fun main591() = runBlocking {
    val a: Int? = listOf(10).asFlow().singleOrNull() // return 10
    val b: Int? = listOf<Int>().asFlow().singleOrNull() // return null because has no element
    listOf(1, 2).asFlow().singleOrNull() // throw Exception because has more than 1 element
    println(a.toString()) // print 10
    println(b.toString()) // print null
}

// flow1.zip(flow2) -> new flow, zip will wait until flow1 and flow2 finish emitted single element then combine to be new element
fun main592() = runBlocking<Unit> {
    val nums = (1..3).asFlow() // numbers 1..3
    val strs = flowOf("one", "two", "three") // strings
    nums.zip(strs) { a, b -> "$a -> $b" } // compose a single string
        .collect { println(it) } // collect and print
}

// flow1.combine(flow2) combine will combine whenever flow1 or flow2 emit element, not same to zip wait unit flow1 and flow emitted element
fun main593() = runBlocking<Unit> {
    val nums = (1..3).asFlow().onEach { delay(300) } // numbers 1..3 every 300 ms
    val strs = flowOf("one", "two", "three").onEach { delay(400) } // strings every 400 ms
    val startTime = System.currentTimeMillis() // remember the start time
    nums.combine(strs) { a, b -> "$a -> $b" } // compose a single string with "combine"
        .collect { value -> // collect and print
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}

fun requestFlow(i: Int): Flow<String> = flow { // flowB
    emit("$i: First")
    delay(500) // wait 500 ms
    emit("$i: Second")
}

// flowA.flatMapConcat(FlowB) -> wait for flowB finish emitted then collect next element from flowA
fun main594() = runBlocking<Unit> {
    val startTime = System.currentTimeMillis() // remember the start time
    // flowA
    (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
        .flatMapConcat { requestFlow(it) }
        .collect { value -> // collect and print
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}

//  flowA.flatMapMerge(FlowB) -> will emit new element as soon as it emit from flowB, it not wait until flowB finish emit then collect from flowA as flatMapConcat
fun main595() = runBlocking<Unit> {
    val startTime = System.currentTimeMillis() // remember the start time
    // flowA
    (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
        .flatMapMerge { requestFlow(it) }
        .collect { value -> // collect and print
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}
// flowA.flatMapLatest(FlowB) -> it will cancel delay emit in flowB to emit new element from flow A, delay() in flowB is suspend function will be cancelled to emit new element
// without delay() in flowB it will print out all element emitted from flow B
fun main596() = runBlocking<Unit> {
    val startTime = System.currentTimeMillis() // remember the start time
    // flowA
    (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
        .flatMapLatest { requestFlow(it) }
        .collect { value -> // collect and print
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}

