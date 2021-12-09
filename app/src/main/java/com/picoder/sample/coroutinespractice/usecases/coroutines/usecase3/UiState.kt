package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase3

import com.picoder.sample.coroutinespractice.mock.VersionFeatures

sealed class UiState {
    object Loading : UiState()
    data class Success(
        val versionFeatures: List<VersionFeatures>
    ) : UiState()

    data class Error(val message: String) : UiState()
}