package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase6

import com.google.gson.Gson
import com.picoder.sample.coroutinespractice.mock.createMockApi
import com.picoder.sample.coroutinespractice.mock.mockAndroidVersions
import com.picoder.sample.coroutinespractice.utils.MockNetworkInterceptor

fun mockApi() = createMockApi(
    MockNetworkInterceptor()
        .mock(
            "http://localhost/recent-android-versions",
            "something went wrong on server side",
            500,
            1000,
            persist = false
        ).mock(
            "http://localhost/recent-android-versions",
            "something went wrong on server side",
            500,
            1000,
            persist = false
        ).mock(
            "http://localhost/recent-android-versions",
            Gson().toJson(mockAndroidVersions),
            200,
            1000
        )
)