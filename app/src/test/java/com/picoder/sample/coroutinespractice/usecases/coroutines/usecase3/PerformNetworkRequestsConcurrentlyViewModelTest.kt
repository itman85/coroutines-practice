package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase3

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.picoder.sample.coroutinespractice.mock.mockVersionFeaturesAndroid10
import com.picoder.sample.coroutinespractice.mock.mockVersionFeaturesOreo
import com.picoder.sample.coroutinespractice.mock.mockVersionFeaturesPie
import com.picoder.sample.coroutinespractice.utils.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class PerformNetworkRequestsConcurrentlyViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val mainCoroutineScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `performNetworkRequestsSequentially should return data after 3 times the response delay`() =
        mainCoroutineScopeRule.runBlockingTest {
            val responseDelay = 1000L
            val fakeApi = FakeSuccessApi(responseDelay)
            val viewModel = PerformNetworkRequestsConcurrentlyViewModel(fakeApi)
            viewModel.observe()

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.performNetworkRequestsSequentially()

            val forwardedTime = advanceUntilIdle() // virtual time advance to last delay

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(
                        listOf(
                            mockVersionFeaturesOreo,
                            mockVersionFeaturesPie,
                            mockVersionFeaturesAndroid10
                        )
                    )
                ),
                receivedUiStates
            )

            // Verify that requests actually got executed sequentially and it took
            // 3000ms to receive all data for 3 request each delay 1000ms
            Assert.assertEquals(
                3000,
                forwardedTime
            )
        }

    @Test
    fun `performNetworkRequestsConcurrently should return data after the response delay`() =
        mainCoroutineScopeRule.runBlockingTest {
            val responseDelay = 1000L
            val fakeApi = FakeSuccessApi(responseDelay)
            val viewModel = PerformNetworkRequestsConcurrentlyViewModel(fakeApi)
            viewModel.observe()

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.performNetworkRequestsConcurrently()

            val forwardedTime = advanceUntilIdle()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(
                        listOf(
                            mockVersionFeaturesOreo,
                            mockVersionFeaturesPie,
                            mockVersionFeaturesAndroid10
                        )
                    )
                ),
                receivedUiStates
            )

            // Verify that requests actually got executed concurrently within 1000ms
            Assert.assertEquals(
                1000,
                forwardedTime
            )
        }

    @Test
    fun `performNetworkRequestsConcurrently should return Error when there is a network request fails`() =
        mainCoroutineScopeRule.runBlockingTest {
            val responseDelay = 1000L
            val fakeApi = FakeErrorApi(responseDelay)
            val viewModel = PerformNetworkRequestsConcurrentlyViewModel(fakeApi)
            viewModel.observe()

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.performNetworkRequestsConcurrently()

            advanceUntilIdle()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error("Network Request failed")
                ),
                receivedUiStates
            )
        }

    @Test
    fun `performNetworkRequestsConcurrently should continue even there is a network request fails`() =
        mainCoroutineScopeRule.runBlockingTest {
            val responseDelay = 1000L
            val fakeApi = FakeErrorApi(responseDelay)
            val viewModel = PerformNetworkRequestsConcurrentlyViewModel(fakeApi)
            viewModel.observe()

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.performNetworkRequestsConcurrentlyEvenOneFailed()

            advanceUntilIdle() // *** it need to advance virtual time clock to last delay of requests

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(
                        listOf(
                            mockVersionFeaturesOreo,
                            mockVersionFeaturesAndroid10
                        )
                    )
                ),
                receivedUiStates
            )
        }

    private fun PerformNetworkRequestsConcurrentlyViewModel.observe() {
        uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }
}