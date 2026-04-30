package com.cret.hilt_practice

import com.cret.hilt_practice.domain.error.UserNotFoundException
import com.cret.hilt_practice.domain.model.UserProfile
import com.cret.hilt_practice.domain.repository.UserRepository
import com.cret.hilt_practice.domain.usecase.GetUserProfileUseCase
import com.cret.hilt_practice.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
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
    fun fetchUser_updatesUiState_withConstructorInjectedRepository() = runTest {
        val fakeRepository = object : UserRepository {
            override suspend fun getUser(userId: String): UserProfile {
                return UserProfile(
                    id = userId,
                    name = "Test User",
                    email = "test@example.com"
                )
            }
        }

        val viewModel = UserViewModel(GetUserProfileUseCase(fakeRepository))

        viewModel.fetchUser("test-id")
        testDispatcher.scheduler.advanceUntilIdle()

        val user = viewModel.uiState.value.user

        assertEquals("test-id", user?.id)
        assertEquals("Test User", user?.name)
        assertEquals("test@example.com", user?.email)
    }

    @Test
    fun fetchUser_updatesUiState_withError_whenRepositoryFails() = runTest {
        val fakeRepository = object : UserRepository {
            override suspend fun getUser(userId: String): UserProfile {
                throw UserNotFoundException(userId)
            }
        }

        val viewModel = UserViewModel(GetUserProfileUseCase(fakeRepository))

        viewModel.fetchUser("broken-id")
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.value

        assertFalse(uiState.isLoading)
        assertNull(uiState.user)
        assertEquals(R.string.profile_error_user_not_found, uiState.errorMessageRes)
    }

    @Test
    fun showMissingUserIdError_updatesUiState_withMissingUserIdMessage() {
        val fakeRepository = object : UserRepository {
            override suspend fun getUser(userId: String): UserProfile {
                error("Repository should not be called")
            }
        }
        val viewModel = UserViewModel(GetUserProfileUseCase(fakeRepository))

        viewModel.showMissingUserIdError()

        val uiState = viewModel.uiState.value

        assertFalse(uiState.isLoading)
        assertNull(uiState.user)
        assertEquals(R.string.profile_error_missing_user_id, uiState.errorMessageRes)
    }
}
