package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase13

import com.picoder.sample.coroutinespractice.base.BaseViewModel
import com.picoder.sample.coroutinespractice.mock.MockApi

class ExceptionHandlingViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun handleExceptionWithTryCatch() {

    }

    fun handleWithCoroutineExceptionHandler() {

    }

    fun showResultsEvenIfChildCoroutineFails() {

    }
}