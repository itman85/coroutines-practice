package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase10

import androidx.lifecycle.viewModelScope
import com.picoder.sample.coroutinespractice.base.BaseViewModel
import kotlinx.coroutines.*
import java.math.BigInteger
import kotlin.system.measureTimeMillis

class CalculationInBackgroundViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseViewModel<UiState>() {

    fun performCalculation(factorialOf: Int) {
        uiState.value = UiState.Loading
        // viewModelScope has coroutine context run on Main thread, so if coroutine run in this scope not define it's own context
        // then it will inherit viewModelScope coroutine context
        // each coroutine can define it's own context (default Main thread)
        // if set viewModelScope.launch(context = Dispatchers.Default)  it will run on worker thread -> cannot update ui state in worker theard
        viewModelScope.launch(context = Dispatchers.Main) {
            try {
                var result: BigInteger = BigInteger.ZERO
                val computationDuration = measureTimeMillis {
                    result = calculateFactorialOf(factorialOf)
                }

                var resultString = ""
                val stringConversionDuration = measureTimeMillis {
                    resultString = convertToString(result)
                }

                // this only work in main thread (coroutine context Main)
                uiState.value =
                    UiState.Success(resultString, computationDuration, stringConversionDuration)
            } catch (exception: Exception) {
                uiState.value = UiState.Error("Error while calculating result")
            }
        }
        //uiState.value = UiState.Loading
    }

    // factorial of n (n!) = 1 * 2 * 3 * 4 * ... * n
    private suspend fun calculateFactorialOf(number: Int): BigInteger {
        // make this block code run under coroutine context (main or worker thread)
        // if it run in Dispatchers.Main will block
        return withContext(Dispatchers.Main) {
            var factorial = BigInteger.ONE
            for (i in 1..number) {
                factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
            }
            factorial
        }
    }

    private fun calculateFactorialOf1(number: Int): BigInteger {
        var factorial = BigInteger.ONE
        for (i in 1..number) {
            factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
        }
        return factorial
    }

    private suspend fun convertToString(number: BigInteger): String {
        // make this block code run in background thread
        return withContext(Dispatchers.Main) {
            number.toString()
        }
    }

    private fun convertToString1(number: BigInteger): String {
        // make this block code run in background thread
        return number.toString()
    }
}