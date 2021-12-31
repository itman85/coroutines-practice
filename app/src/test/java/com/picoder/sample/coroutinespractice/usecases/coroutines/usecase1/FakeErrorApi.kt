package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase1

import com.picoder.sample.coroutinespractice.mock.AndroidVersion
import com.picoder.sample.coroutinespractice.mock.MockApi
import com.picoder.sample.coroutinespractice.mock.VersionFeatures
import com.picoder.sample.coroutinespractice.utils.EndpointShouldNotBeCalledException
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class FakeErrorApi() : MockApi {

    override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
        throw HttpException(
            Response.error<List<AndroidVersion>>(
                500,
                ResponseBody.create(MediaType.parse("application/json"), "")
            )
        )
    }

    override suspend fun getAndroidVersionFeatures(apiLevel: Int): VersionFeatures {
        throw EndpointShouldNotBeCalledException()
    }
}