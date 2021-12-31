package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase1

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.picoder.sample.coroutinespractice.mock.mockAndroidVersions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class PerformSingleNetworkRequestViewModelTest {

    // this rule help to run this test off main thread (avoid getLooper main thread from view model)
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule() // this come from androidx.arch.core:core-testing

   private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp(){
        // this fix error: Module with the Main dispatcher had failed to initialize. For tests Dispatchers.setMain from kotlinx-coroutines-test module can be used
        Dispatchers.setMain(dispatcher) // make test run synchronously, if not set this then assertion will run before code in coroutine executed
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }
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