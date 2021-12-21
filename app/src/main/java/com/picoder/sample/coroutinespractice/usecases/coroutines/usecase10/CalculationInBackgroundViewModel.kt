package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase10

import androidx.lifecycle.viewModelScope
import com.picoder.sample.coroutinespractice.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import kotlin.system.measureTimeMillis

class CalculationInBackgroundViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseViewModel<UiState>() {

    fun performCalculation(factorialOf: Int) {
        uiState.value = UiState.Loading
        // viewModelScope has coroutine context run on Main thread, so if coroutine run in this scope not define it's own context
        // then it will inherit viewModelScope coroutine context
        // each coroutine can define it's own context
        viewModelScope.launch {
            try {
                var result: BigInteger = BigInteger.ZERO
                val computationDuration = measureTimeMillis {
                    result = calculateFactorialOf(factorialOf)
                }

                var resultString = ""
                val stringConversionDuration = measureTimeMillis {
                    resultString = convertToString(result)
                }

                uiState.value =
                    UiState.Success(resultString, computationDuration, stringConversionDuration)
            } catch (exception: Exception) {
                UiState.Error("Error while calculating result")
            }
        }
    }

    // factorial of n (n!) = 1 * 2 * 3 * 4 * ... * n
    private suspend fun calculateFactorialOf(number: Int): BigInteger =
        // make this block code run in background thread
        withContext(defaultDispatcher) {
            var factorial = BigInteger.ONE
            for (i in 1..number) {
                factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
            }
            factorial
        }

    private suspend fun convertToString(number: BigInteger): String =
        // make this block code run in background thread
        withContext(defaultDispatcher) {
            number.toString()
        }
}