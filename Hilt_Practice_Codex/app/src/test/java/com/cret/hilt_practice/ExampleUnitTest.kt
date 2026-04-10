package com.cret.hilt_practice

import com.cret.hilt_practice.data.model.User
import com.cret.hilt_practice.data.repository.UserRepository
import com.cret.hilt_practice.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExampleUnitTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchUser_updatesUiState_withManualDependencyInjection() = runTest {
        val fakeRepository = object : UserRepository {
            override suspend fun getUser(userId: String): User {
                return User(
                    id = userId,
                    name = "Test User",
                    email = "test@example.com"
                )
            }
        }

        val viewModel = UserViewModel(repository = fakeRepository)

        viewModel.fetchUser("test-id")
        testDispatcher.scheduler.advanceUntilIdle()

        val user = viewModel.uiState.value.user

        assertEquals("test-id", user?.id)
        assertEquals("Test User", user?.name)
        assertEquals("test@example.com", user?.email)
    }
}
