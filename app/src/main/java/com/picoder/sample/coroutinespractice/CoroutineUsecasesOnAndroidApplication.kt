package com.picoder.sample.coroutinespractice

import android.app.Application
import androidx.viewbinding.BuildConfig

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

class CoroutineUsecasesOnAndroidApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())


    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        // Enable Debugging for Kotlin Coroutines in debug builds
        // Prints Coroutine name when logging Thread.currentThread().name
        System.setProperty("kotlinx.coroutines.debug", if (BuildConfig.DEBUG) "on" else "off")
    }
}