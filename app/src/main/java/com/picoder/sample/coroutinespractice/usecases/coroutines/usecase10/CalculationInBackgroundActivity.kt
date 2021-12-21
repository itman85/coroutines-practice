package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase10

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.picoder.sample.coroutinespractice.R
import com.picoder.sample.coroutinespractice.base.BaseActivity
import com.picoder.sample.coroutinespractice.base.useCase10Description
import com.picoder.sample.coroutinespractice.databinding.ActivityCalculationinbackgroundBinding
import com.picoder.sample.coroutinespractice.utils.hideKeyboard
import com.picoder.sample.coroutinespractice.utils.setGone
import com.picoder.sample.coroutinespractice.utils.setVisible
import com.picoder.sample.coroutinespractice.utils.toast

class CalculationInBackgroundActivity : BaseActivity() {

    override fun getToolbarTitle() = useCase10Description

    private val binding by lazy { ActivityCalculationinbackgroundBinding.inflate(layoutInflater) }
    private val viewModel: CalculationInBackgroundViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.uiState().observe(this, Observer { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })
        binding.btnCalculate.setOnClickListener {
            val factorialOf = binding.editTextFactorialOf.text.toString().toIntOrNull()
            if (factorialOf != null) {
                viewModel.performCalculation(factorialOf)
            }
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
        textViewCalculationDuration.text = ""
        textViewStringConversionDuration.text = ""
        btnCalculate.isEnabled = false
        textViewResult.hideKeyboard()
    }

    private fun onSuccess(uiState: UiState.Success) = with(binding) {
        textViewCalculationDuration.text =
            getString(R.string.duration_calculation, uiState.computationDuration)

        textViewStringConversionDuration.text =
            getString(R.string.duration_stringconversion, uiState.stringConversionDuration)

        progressBar.setGone()
        btnCalculate.isEnabled = true
        textViewResult.text = if (uiState.result.length <= 150) {
            uiState.result
        } else {
            "${uiState.result.substring(0, 147)}..."
        }
    }

    private fun onError(uiState: UiState.Error) = with(binding) {
        progressBar.setGone()
        btnCalculate.isEnabled = true
        toast(uiState.message)
    }
}