package com.cret.hilt_practice

import com.cret.hilt_practice.data.source.LocalUserDataSource
import com.cret.hilt_practice.domain.repository.UserRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class HiltGraphTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun hiltGraph_providesUserRepository() = runTest {
        val user = userRepository.getUser(LocalUserDataSource.DEFAULT_USER_ID)

        assertEquals(LocalUserDataSource.DEFAULT_USER_ID, user.id)
    }
}
