package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase1

import com.picoder.sample.coroutinespractice.mock.AndroidVersion
import com.picoder.sample.coroutinespractice.mock.MockApi
import com.picoder.sample.coroutinespractice.mock.VersionFeatures
import com.picoder.sample.coroutinespractice.mock.mockAndroidVersions
import com.picoder.sample.coroutinespractice.utils.EndpointShouldNotBeCalledException

class FakeSuccessApi() : MockApi {

    override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
        return mockAndroidVersions
    }

    override suspend fun getAndroidVersionFeatures(apiLevel: Int): VersionFeatures {
        throw EndpointShouldNotBeCalledException()
    }
}