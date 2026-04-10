package com.cret.hilt_practice.data.repository

import com.cret.hilt_practice.data.model.User

class FakeUserRepository : UserRepository {
    override suspend fun getUser(userId: String): User {
        return User(
            id = userId,
            name = "Codex Student",
            email = "student@example.com"
        )
    }
}
