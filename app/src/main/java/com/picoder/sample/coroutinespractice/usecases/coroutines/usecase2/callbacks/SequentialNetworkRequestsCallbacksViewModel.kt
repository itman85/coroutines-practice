package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase2.callbacks

import com.picoder.sample.coroutinespractice.base.BaseViewModel


class SequentialNetworkRequestsCallbacksViewModel(
    private val mockApi: CallbackMockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {

    }
}