package com.cret.hilt_practice.domain.usecase

import com.cret.hilt_practice.data.model.User
import com.cret.hilt_practice.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetUserUseCaseTest {

    private val repository: UserRepository = mockk()
    private lateinit var useCase: GetUserUseCase

    @Before
    fun setup() {
        useCase = GetUserUseCase(repository)
    }

    @Test
    fun `repository 성공 결과를 그대로 반환한다`() = runTest {
        val user = User(id = "123", name = "Mock User")
        coEvery { repository.getUser("123") } returns Result.success(user)

        val result = useCase("123")

        assertEquals(Result.success(user), result)
    }

    @Test
    fun `repository 실패 결과를 그대로 반환한다`() = runTest {
        val exception = RuntimeException("오류")
        coEvery { repository.getUser(any()) } returns Result.failure(exception)

        val result = useCase("123")

        assertTrue(result.isFailure)
    }
}
