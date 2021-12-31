package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase1

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.picoder.sample.coroutinespractice.mock.mockAndroidVersions
import com.picoder.sample.coroutinespractice.utils.MainCoroutineScopeRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class PerformSingleNetworkRequestViewModelTest1 {

    // this rule help to run this test off main thread (avoid getLooper main thread from view model)
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule() // this come from androidx.arch.core:core-testing

    @get: Rule
    val mainCoroutineScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()

    @Test
    fun `test perform single network request - success`() {
        val mockApi = FakeSuccessApi()
        val viewModel = PerformSingleNetworkRequestViewModel(mockApi)
        val receivedUiStates = mutableListOf<UiState>()
        //
        viewModel.uiState().observeForever {
            receivedUiStates.add(it)
        }
        //
        viewModel.performSingleNetworkRequest()
        //
        Assert.assertEquals(
            listOf(UiState.Loading,UiState.Success(mockAndroidVersions)),
            receivedUiStates
        )
    }

    @Test
    fun `test perform single network request - failed`() {
        val mockApi = FakeErrorApi()
        val viewModel = PerformSingleNetworkRequestViewModel(mockApi)
        val receivedUiStates = mutableListOf<UiState>()
        //
        viewModel.uiState().observeForever {
            receivedUiStates.add(it)
        }
        //
        viewModel.performSingleNetworkRequest()
        //
        Assert.assertEquals(
            listOf(UiState.Loading,UiState.Error("Network Request failed!")),
            receivedUiStates
        )
    }
}