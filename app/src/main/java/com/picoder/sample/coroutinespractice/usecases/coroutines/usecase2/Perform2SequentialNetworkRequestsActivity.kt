package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase2

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.picoder.sample.coroutinespractice.base.BaseActivity
import com.picoder.sample.coroutinespractice.base.useCase2Description
import com.picoder.sample.coroutinespractice.databinding.ActivityPerform2sequentialnetworkrequestsBinding
import com.picoder.sample.coroutinespractice.utils.fromHtml
import com.picoder.sample.coroutinespractice.utils.setGone
import com.picoder.sample.coroutinespractice.utils.setVisible
import com.picoder.sample.coroutinespractice.utils.toast


class Perform2SequentialNetworkRequestsActivity : BaseActivity() {

    private val binding by lazy {
        ActivityPerform2sequentialnetworkrequestsBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: Perform2SequentialNetworkRequestsViewModel by viewModels()

    override fun getToolbarTitle() = useCase2Description

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.uiState().observe(this, Observer { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })
        binding.btnRequestsSequentially.setOnClickListener {
            viewModel.perform2SequentialNetworkRequest()
        }
    }

    private fun render(uiState: UiState) {
        when (uiState) {
            is UiState.Loading -> {
                onLoad()
            }
            is UiState.Success -> {
                onSuccess(uiState)
            }
            is UiState.Error -> {
                onError(uiState)
            }
        }
    }

    private fun onLoad() = with(binding) {
        progressBar.setVisible()
        textViewResult.text = ""
    }

    private fun onSuccess(uiState: UiState.Success) = with(binding) {
        progressBar.setGone()
        textViewResult.text = fromHtml(
            "<b>Features of most recent Android Version \" ${uiState.versionFeatures.androidVersion.name} \"</b><br>" +
                    uiState.versionFeatures.features.joinToString(
                        prefix = "- ",
                        separator = "<br>- "
                    )
        )
    }

    private fun onError(uiState: UiState.Error) = with(binding) {
        progressBar.setGone()
        btnRequestsSequentially.isEnabled = true
        toast(uiState.message)
    }
}