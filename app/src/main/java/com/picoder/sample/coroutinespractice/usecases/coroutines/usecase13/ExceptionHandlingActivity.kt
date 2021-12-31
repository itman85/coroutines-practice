package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase13

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.picoder.sample.coroutinespractice.R
import com.picoder.sample.coroutinespractice.base.BaseActivity
import com.picoder.sample.coroutinespractice.base.useCase13Description
import com.picoder.sample.coroutinespractice.databinding.ActivityExceptionhandlingBinding
import com.picoder.sample.coroutinespractice.utils.fromHtml
import com.picoder.sample.coroutinespractice.utils.setGone
import com.picoder.sample.coroutinespractice.utils.setVisible
import com.picoder.sample.coroutinespractice.utils.toast

class ExceptionHandlingActivity : BaseActivity() {

    private val binding by lazy {
        ActivityExceptionhandlingBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: ExceptionHandlingViewModel by viewModels()
    override fun getToolbarTitle() = useCase13Description

    private var operationStartTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.uiState().observe(this, Observer { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })
        binding.btnExceptionTryCatch.setOnClickListener {
            viewModel.handleExceptionWithTryCatch()
        }
        binding.btnCoroutineExceptionHandler.setOnClickListener {
            viewModel.handleWithCoroutineExceptionHandler()
        }
        binding.btnShowResultsEvenIfChildCoroutineFailsTryCatch.setOnClickListener {
            viewModel.showResultsEvenIfChildCoroutineFails()
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
        operationStartTime = System.currentTimeMillis()
        progressBar.setVisible()
        textViewDuration.text = ""
        textViewResult.text = ""
        disableButtons()
    }

    private fun onSuccess(uiState: UiState.Success) = with(binding) {
        enableButtons()
        progressBar.setGone()
        val duration = System.currentTimeMillis() - operationStartTime
        textViewDuration.text = getString(R.string.duration, duration)

        val versionFeatures = uiState.versionFeatures
        val versionFeaturesString = versionFeatures.joinToString(separator = "<br><br>") {
            "<b>New Features of ${it.androidVersion.name} </b> <br> ${it.features.joinToString(
                prefix = "- ",
                separator = "<br>- "
            )}"
        }

        textViewResult.text = if(versionFeaturesString.isEmpty()) "" else fromHtml(versionFeaturesString)
    }

    private fun onError(uiState: UiState.Error) = with(binding) {
        progressBar.setGone()
        textViewDuration.setGone()
        toast(uiState.message)
        enableButtons()
    }

    private fun enableButtons() = with(binding) {
        btnExceptionTryCatch.isEnabled = true
        btnCoroutineExceptionHandler.isEnabled = true
        btnShowResultsEvenIfChildCoroutineFailsTryCatch.isEnabled = true
    }

    private fun disableButtons() = with(binding) {
        btnExceptionTryCatch.isEnabled = false
        btnCoroutineExceptionHandler.isEnabled = false
        btnShowResultsEvenIfChildCoroutineFailsTryCatch.isEnabled = false
    }
}