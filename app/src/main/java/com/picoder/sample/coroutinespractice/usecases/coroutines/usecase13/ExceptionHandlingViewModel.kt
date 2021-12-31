package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase13

import androidx.lifecycle.viewModelScope
import com.picoder.sample.coroutinespractice.base.BaseViewModel
import com.picoder.sample.coroutinespractice.mock.MockApi
import com.picoder.sample.coroutinespractice.mock.VersionFeatures
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

class ExceptionHandlingViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun handleExceptionWithTryCatch() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                api.getAndroidVersionFeatures(27)

            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed: $exception")
            }
        }
    }

    fun handleWithCoroutineExceptionHandler() {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            uiState.value = UiState.Error("Network Request failed!! $exception")
        }

        uiState.value = UiState.Loading
        viewModelScope.launch(exceptionHandler) {
            api.getAndroidVersionFeatures(27)
        }
    }

    fun showResultsEvenIfChildCoroutineFails1() {
        uiState.value = UiState.Loading
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            uiState.value = UiState.Error("Network Request failed!! $exception")
        }
        viewModelScope.launch(exceptionHandler) {

            // with supervisorScope if child coroutine fail, it will not propagate cancellation to sibling coroutines
            // without supervisorScope it will cancel sibling coroutine if there is failed coroutine even try catch
            // try catch only help code will continue run but it will propagate error to top level coroutine
            // if not try catch it will stop and propagate error to top level coroutine immediately
            // why launch (SupervisorJob()) {} !=  supervisorScope{}
            supervisorScope {
                val oreoFeaturesDeferred = async { api.getAndroidVersionFeatures(27) }
                val pieFeaturesDeferred = async { api.getAndroidVersionFeatures(28) }
                val android10FeaturesDeferred = async { api.getAndroidVersionFeatures(29) }

                val versionFeatures = listOf(
                    oreoFeaturesDeferred,
                    pieFeaturesDeferred,
                    android10FeaturesDeferred
                ).mapNotNull {
                    try {
                        it.await()
                    } catch (e: Exception) {
                        null
                    }
                }
                uiState.value = UiState.Success(versionFeatures)
            }
        }
    }

    fun showResultsEvenIfChildCoroutineFails() {
        uiState.value = UiState.Loading
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            uiState.value = UiState.Error("Network Request failed!! $exception")
        }
        val data = mutableListOf<VersionFeatures>()
        viewModelScope.launch(SupervisorJob() + exceptionHandler) {
            launch {
                val resDeferred = async { api.getAndroidVersionFeatures(27) }
                try {
                    val res = resDeferred.await()
                    uiState.value = UiState.Success(listOf(res))
                }catch (ex:Exception){
                    println("Coroutine 2 got cancelled!")
                }
            }

            launch {
                val resDeferred = async { api.getAndroidVersionFeatures(28) }
                val res = resDeferred.await()
                uiState.value = UiState.Success(listOf(res))
            }.invokeOnCompletion {
                if (it is CancellationException) {
                    println("Coroutine 2 got cancelled!")
                }
            }

            launch {
                val resDeferred = async { api.getAndroidVersionFeatures(29) }
                val res = resDeferred.await()
                uiState.value = UiState.Success(listOf(res))
            }.invokeOnCompletion {
                if (it is CancellationException) {
                    println("Coroutine 2 got cancelled!")
                }
            }
        }
    }
}