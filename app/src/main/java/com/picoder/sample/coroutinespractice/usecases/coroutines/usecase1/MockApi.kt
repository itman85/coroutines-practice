package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase1

import com.google.gson.Gson
import com.picoder.sample.coroutinespractice.mock.createMockApi
import com.picoder.sample.coroutinespractice.mock.mockAndroidVersions
import com.picoder.sample.coroutinespractice.utils.MockNetworkInterceptor


fun mockApi() =
    createMockApi(
        MockNetworkInterceptor()
            .mock(
                "http://localhost/recent-android-versions",
                Gson().toJson(mockAndroidVersions),
                200,
                15000
            )
    )