package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase3

import com.google.gson.Gson
import com.picoder.sample.coroutinespractice.mock.createMockApi
import com.picoder.sample.coroutinespractice.mock.mockVersionFeaturesAndroid10
import com.picoder.sample.coroutinespractice.mock.mockVersionFeaturesOreo
import com.picoder.sample.coroutinespractice.mock.mockVersionFeaturesPie
import com.picoder.sample.coroutinespractice.utils.MockNetworkInterceptor

fun mockApi() = createMockApi(
    MockNetworkInterceptor()
        .mock(
            "http://localhost/android-version-features/27",
            Gson().toJson(mockVersionFeaturesOreo),
            200,
            1000
        )
        .mock(
            "http://localhost/android-version-features/28",
            Gson().toJson(mockVersionFeaturesPie),
            200,
            1000
        )
        .mock(
            "http://localhost/android-version-features/29",
            Gson().toJson(mockVersionFeaturesAndroid10),
            200,
            1000
        )
)