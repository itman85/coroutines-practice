package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase7

import com.google.gson.Gson
import com.picoder.sample.coroutinespractice.mock.createMockApi
import com.picoder.sample.coroutinespractice.mock.mockVersionFeaturesOreo
import com.picoder.sample.coroutinespractice.mock.mockVersionFeaturesPie
import com.picoder.sample.coroutinespractice.utils.MockNetworkInterceptor

fun mockApi() = createMockApi(
    MockNetworkInterceptor()
        // timeout on first request for oreo features
        .mock(
            "http://localhost/android-version-features/27",
            Gson().toJson(mockVersionFeaturesOreo),
            200,
            1050,
            persist = false
        )
        // network error on second request
        .mock(
            "http://localhost/android-version-features/27",
            "Something went wrong on servers side",
            500,
            100,
            persist = false
        )
        // 3rd request is successful and within timeout
        .mock(
            "http://localhost/android-version-features/27",
            Gson().toJson(mockVersionFeaturesOreo),
            200,
            100
        )
        // timeout on first request for pie features
        .mock(
            "http://localhost/android-version-features/28",
            Gson().toJson(mockVersionFeaturesPie),
            200,
            1050,
            persist = false
        )
        // network error on second request
        .mock(
            "http://localhost/android-version-features/28",
            "Something went wrong on servers side",
            500,
            100,
            persist = false
        )
        // 3rd request is successful and within timeout
        .mock(
            "http://localhost/android-version-features/28",
            Gson().toJson(mockVersionFeaturesPie),
            200,
            100
        )
)