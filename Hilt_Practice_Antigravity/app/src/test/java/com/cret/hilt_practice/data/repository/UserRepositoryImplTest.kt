package com.cret.hilt_practice.data.repository

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UserRepositoryImplTest {

    private val repository = UserRepositoryImpl()

    @Test
    fun `getUser는 성공 Result를 반환한다`() = runTest {
        val result = repository.getUser("123")
        assertTrue(result.isSuccess)
    }

    @Test
    fun `getUser는 요청한 id를 가진 User를 반환한다`() = runTest {
        val result = repository.getUser("123")
        assertEquals("123", result.getOrNull()?.id)
    }

    @Test
    fun `getUser 결과의 User는 null이 아니다`() = runTest {
        val result = repository.getUser("any_id")
        assertNotNull(result.getOrNull())
    }
}
