package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase6

import com.picoder.sample.coroutinespractice.mock.AndroidVersion
import com.picoder.sample.coroutinespractice.mock.MockApi
import com.picoder.sample.coroutinespractice.mock.VersionFeatures
import com.picoder.sample.coroutinespractice.mock.mockAndroidVersions
import com.picoder.sample.coroutinespractice.utils.EndpointShouldNotBeCalledException
import kotlinx.coroutines.delay
import java.io.IOException

class FakeSuccessOnThirdAttemptApi(private val responseDelay: Long) : MockApi {

    var requestCount = 0

    override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
        requestCount++
        delay(responseDelay)
        if (requestCount < 3) {
            throw IOException()
        }
        return mockAndroidVersions
    }

    override suspend fun getAndroidVersionFeatures(apiLevel: Int): VersionFeatures {
        throw EndpointShouldNotBeCalledException()
    }
}