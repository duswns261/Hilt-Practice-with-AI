package com.cret.hilt_practice.data.source

import com.cret.hilt_practice.data.model.User
import com.cret.hilt_practice.domain.error.UserNotFoundException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalUserDataSource @Inject constructor() : UserDataSource {
    private val users = listOf(
        User(
            id = DEFAULT_USER_ID,
            name = "Codex Student",
            email = "student@example.com"
        )
    ).associateBy(User::id)

    override suspend fun getUser(userId: String): User {
        return users[userId] ?: throw UserNotFoundException(userId)
    }

    companion object {
        const val DEFAULT_USER_ID = "codex-student"
    }
}
