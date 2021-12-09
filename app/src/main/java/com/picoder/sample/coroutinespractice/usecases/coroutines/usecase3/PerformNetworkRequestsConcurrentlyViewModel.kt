package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase3

import com.picoder.sample.coroutinespractice.base.BaseViewModel
import com.picoder.sample.coroutinespractice.mock.MockApi


class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {

    }

    fun performNetworkRequestsConcurrently() {

    }
}