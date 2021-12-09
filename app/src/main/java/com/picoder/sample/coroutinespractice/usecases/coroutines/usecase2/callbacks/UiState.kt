package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase2.callbacks

import com.picoder.sample.coroutinespractice.mock.VersionFeatures

sealed class UiState {
    object Loading : UiState()
    data class Success(
        val versionFeatures: VersionFeatures
    ) : UiState()

    data class Error(val message: String) : UiState()
}