package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase2.rx

import com.picoder.sample.coroutinespractice.base.BaseViewModel


class SequentialNetworkRequestsRxViewModel(
    private val mockApi: RxMockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {

    }
}