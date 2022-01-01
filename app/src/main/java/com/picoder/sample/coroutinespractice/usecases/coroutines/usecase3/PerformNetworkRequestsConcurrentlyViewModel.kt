package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.picoder.sample.coroutinespractice.base.BaseViewModel
import com.picoder.sample.coroutinespractice.mock.MockApi
import kotlinx.coroutines.*


class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val oreoFeatures = mockApi.getAndroidVersionFeatures(27)
                val pieFeatures = mockApi.getAndroidVersionFeatures(28)
                val android10Features = mockApi.getAndroidVersionFeatures(29)

                val versionFeatures = listOf(oreoFeatures, pieFeatures, android10Features)
                uiState.value = UiState.Success(versionFeatures)

            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }

    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading

        val oreoFeaturesDeferred = viewModelScope.async { mockApi.getAndroidVersionFeatures(27) }
        val pieFeaturesDeferred = viewModelScope.async { mockApi.getAndroidVersionFeatures(28) }
        val android10FeaturesDeferred =
            viewModelScope.async { mockApi.getAndroidVersionFeatures(29) }

        viewModelScope.launch {
            try {
                val versionFeatures =
                    awaitAll(oreoFeaturesDeferred, pieFeaturesDeferred, android10FeaturesDeferred)
                uiState.value = UiState.Success(versionFeatures)
            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }

    fun performNetworkRequestsConcurrentlyEvenOneFailed() {
        uiState.value = UiState.Loading
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            uiState.value = UiState.Error("Network Request failed!")
        }
        viewModelScope.launch(exceptionHandler) {

            // with supervisorScope if child coroutine fail, it will not propagate cancellation to sibling coroutines
            // without supervisorScope it will cancel sibling coroutine if there is failed coroutine even try catch
            // try catch only help code will continue run but it will propagate error to top level coroutine
            // if not try catch it will stop and propagate error to top level coroutine immediately
            // why launch (SupervisorJob()) {} !=  supervisorScope{}
            supervisorScope {
                val oreoFeaturesDeferred = async { mockApi.getAndroidVersionFeatures(27) }
                val pieFeaturesDeferred = async { mockApi.getAndroidVersionFeatures(28) }
                val android10FeaturesDeferred = async { mockApi.getAndroidVersionFeatures(29) }

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

    fun performNetworkRequestsConcurrently1() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val oreoFeaturesDeferred = async { mockApi.getAndroidVersionFeatures(27) }
                val pieFeaturesDeferred = async { mockApi.getAndroidVersionFeatures(28) }
                val android10FeaturesDeferred = async { mockApi.getAndroidVersionFeatures(29) }

                val oreoFeatures = oreoFeaturesDeferred.await()
                val pieFeatures = pieFeaturesDeferred.await()
                val android10Features = android10FeaturesDeferred.await()

                val versionFeatures = listOf(oreoFeatures, pieFeatures, android10Features)

                // other alternative: (but slightly different behavior when a deferred fails, see docs)
                // val versionFeatures = awaitAll(oreoFeaturesDeferred, pieFeaturesDeferred, android10FeaturesDeferred)

                uiState.value = UiState.Success(versionFeatures)

            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }
}