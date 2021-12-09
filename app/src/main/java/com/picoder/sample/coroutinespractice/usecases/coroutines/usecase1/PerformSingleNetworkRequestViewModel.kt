package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase1

import com.picoder.sample.coroutinespractice.base.BaseViewModel
import com.picoder.sample.coroutinespractice.mock.MockApi


class PerformSingleNetworkRequestViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performSingleNetworkRequest() {

    }
}