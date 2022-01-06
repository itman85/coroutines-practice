package com.picoder.sample.coroutinespractice.usecases.coroutines.usecase14

import com.picoder.sample.coroutinespractice.mock.AndroidVersion
import com.picoder.sample.coroutinespractice.mock.mockAndroidVersions
import com.picoder.sample.coroutinespractice.utils.MainCoroutineScopeRule
import junit.framework.Assert.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AndroidVersionRepositoryTest {

    @get: Rule
    val mainCoroutineScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()

    @Test
    fun `getLocalAndroidVersions() should return android versions from database`() =
        mainCoroutineScopeRule.runBlockingTest {
            val fakeDatabase = FakeDatabase()

            val repository = AndroidVersionRepository(fakeDatabase, mainCoroutineScopeRule)
            assertEquals(mockAndroidVersions, repository.getLocalAndroidVersions())
        }

    @Test
    fun `loadRecentAndroidVersions() should return android versions from network`() =
        mainCoroutineScopeRule.runBlockingTest {
            val fakeDatabase = FakeDatabase()
            val fakeApi = FakeApi()
            val repository = AndroidVersionRepository(
                fakeDatabase,
                mainCoroutineScopeRule,
                api = fakeApi
            )
            assertEquals(mockAndroidVersions, repository.loadAndStoreRemoteAndroidVersions())
        }

    @Test
    fun `loadRecentAndroidVersions() should continue to load and store android versions when calling scope gets cancelled`() =
        mainCoroutineScopeRule.runBlockingTest {
            val fakeDatabase = FakeDatabase()
            val fakeApi = FakeApi()
            val repository = AndroidVersionRepository(
                fakeDatabase,
                mainCoroutineScopeRule,
                api = fakeApi
            )

            var data = listOf<AndroidVersion>()

            // this coroutine will be executed immediately (eagerly)
            // how ever, it will stop its execution at the delay(1) in the fakeApi
            val viewModelScope = TestCoroutineScope(Job())
            viewModelScope.launch {
                println("running coroutine!")
                data = repository.loadAndStoreRemoteAndroidVersions()
                fail("Scope should be cancelled before versions are loaded!")
            }.invokeOnCompletion {
                if(it is CancellationException)
                    println("cancel coroutine!")
            }

            viewModelScope.cancel()

            // continue coroutine execution after delay(1) in the fakeApi
            advanceUntilIdle()

            assertTrue(data.isEmpty())

            assertEquals(true, fakeDatabase.insertedIntoDb)
        }
}