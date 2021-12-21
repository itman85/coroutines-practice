package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase6

import com.picoder.sample.coroutinespractice.mock.AndroidVersion

sealed class UiState {
    object Loading : UiState()
    data class Success(val recentVersions: List<AndroidVersion>) : UiState()
    data class Error(val message: String) : UiState()
}