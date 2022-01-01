package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.picoder.sample.coroutinespractice.mock.AndroidVersion
import com.picoder.sample.coroutinespractice.mock.MockApi
import com.picoder.sample.coroutinespractice.mock.mockAndroidVersions
import com.picoder.sample.coroutinespractice.mock.mockVersionFeaturesAndroid10
import com.picoder.sample.coroutinespractice.utils.EndpointShouldNotBeCalledException
import com.picoder.sample.coroutinespractice.utils.MainCoroutineScopeRule
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.rules.Verifier
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class Perform2SequentialNetworkRequestsViewModelTest {

    private val receivedStates = mutableListOf<UiState>()

    @get:Rule
    val testInstantTaskExecutorRule: TestRule =
        InstantTaskExecutorRule() // this come from androidx.arch.core:core-testing

    @get: Rule
    val mainCoroutineScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()

    @Test
    fun `perform 2 sequential network request - success`() =
        runBlockingTest{
            val mockApi: MockApi = mock()
            val viewModel = Perform2SequentialNetworkRequestsViewModel(mockApi)
            whenever(mockApi.getRecentAndroidVersions()).thenReturn(mockAndroidVersions)
            whenever(mockApi.getAndroidVersionFeatures(any())).thenReturn(
                mockVersionFeaturesAndroid10
            )
            viewModel.uiState().observeForever {
                receivedStates.add(it)
            }
            viewModel.perform2SequentialNetworkRequest()
            Assert.assertEquals(
                listOf(
                    UiState.Loading, UiState.Success(
                        mockVersionFeaturesAndroid10
                    )
                ), receivedStates
            )
            verify(mockApi).getRecentAndroidVersions()
            verify(mockApi).getAndroidVersionFeatures(29)
        }

    @Test
    fun `perform 2 sequential network request - failed`() = mainCoroutineScopeRule.runBlockingTest {
        val mockApi: MockApi = mock()
        val viewModel = Perform2SequentialNetworkRequestsViewModel(mockApi)
        whenever(mockApi.getRecentAndroidVersions()).thenReturn(mockAndroidVersions)
        // throw exception error 500
        whenever(mockApi.getAndroidVersionFeatures(any())).thenThrow(
            HttpException(
                Response.error<List<AndroidVersion>>(
                    500,
                    ResponseBody.create(MediaType.parse("application/json"), "")
                )
            )
        )
        viewModel.uiState().observeForever {
            receivedStates.add(it)
        }
        viewModel.perform2SequentialNetworkRequest()
        Assert.assertEquals(
            listOf(
                UiState.Loading, UiState.Error(
                    "Network Request failed"
                )
            ), receivedStates
        )
        verify(mockApi).getRecentAndroidVersions()
        verify(mockApi).getAndroidVersionFeatures(29)
    }

}