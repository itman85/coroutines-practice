package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase2

import com.picoder.sample.coroutinespractice.base.BaseViewModel
import com.picoder.sample.coroutinespractice.mock.MockApi


class Perform2SequentialNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {

    }
}