package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase2

import com.google.gson.Gson
import com.picoder.sample.coroutinespractice.mock.createMockApi
import com.picoder.sample.coroutinespractice.mock.mockAndroidVersions
import com.picoder.sample.coroutinespractice.mock.mockVersionFeaturesAndroid10
import com.picoder.sample.coroutinespractice.utils.MockNetworkInterceptor

fun mockApi() = createMockApi(
    MockNetworkInterceptor()
        .mock(
            "http://localhost/recent-android-versions",
            Gson().toJson(mockAndroidVersions),
            200,
            1500
        )
        .mock(
            "http://localhost/android-version-features/29",
            Gson().toJson(mockVersionFeaturesAndroid10),
            200,
            1500
        )
)