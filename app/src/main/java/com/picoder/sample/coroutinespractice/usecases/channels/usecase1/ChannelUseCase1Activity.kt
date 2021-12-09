package com.picoder.sample.coroutinespractice.usecases.channels.usecase1

import android.os.Bundle
import com.picoder.sample.coroutinespractice.base.BaseActivity
import com.picoder.sample.coroutinespractice.databinding.ActivityChannelsUsecase1Binding

class ChannelUseCase1Activity : BaseActivity() {

    private val binding by lazy { ActivityChannelsUsecase1Binding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun getToolbarTitle() = "Channel Use Case 1"
}