package com.cret.hilt_practice.presentation.viewmodel

import com.cret.hilt_practice.data.model.User
import com.cret.hilt_practice.data.model.UserError
import com.cret.hilt_practice.domain.usecase.GetUserUseCase
import com.cret.hilt_practice.presentation.model.UserUiModel
import com.cret.hilt_practice.presentation.model.UserUiState
import com.cret.hilt_practice.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getUserUseCase: GetUserUseCase = mockk()
    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        viewModel = UserViewModel(getUserUseCase)
    }

    @Test
    fun `초기 상태는 Loading이다`() {
        assertEquals(UserUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `fetchUser 성공 시 Success 상태가 된다`() = runTest {
        val user = User(id = "123", name = "Mock User")
        coEvery { getUserUseCase("123") } returns Result.success(user)

        viewModel.fetchUser("123")
        advanceUntilIdle()

        val expected = UserUiState.Success(UserUiModel(id = "123", displayName = "Mock User"))
        assertEquals(expected, viewModel.uiState.value)
    }

    @Test
    fun `fetchUser 실패 시 Error 상태가 된다`() = runTest {
        coEvery { getUserUseCase(any()) } returns Result.failure(RuntimeException())

        viewModel.fetchUser("123")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is UserUiState.Error)
    }

    @Test
    fun `fetchUser 실패 시 Unknown 에러 타입이 된다`() = runTest {
        coEvery { getUserUseCase(any()) } returns Result.failure(RuntimeException())

        viewModel.fetchUser("123")
        advanceUntilIdle()

        val state = viewModel.uiState.value as UserUiState.Error
        assertEquals(UserError.Unknown, state.error)
    }

    @Test
    fun `fetchUser 호출 시 Loading 상태로 전환된다`() = runTest {
        coEvery { getUserUseCase(any()) } returns Result.success(User("123", "Mock User"))

        viewModel.fetchUser("123")

        assertEquals(UserUiState.Loading, viewModel.uiState.value)
    }
}
