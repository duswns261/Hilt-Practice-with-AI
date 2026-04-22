package com.cret.hilt_practice.data.repository

import com.cret.hilt_practice.data.model.User
import kotlinx.coroutines.delay

class UserRepositoryImpl : UserRepository {
    override suspend fun getUser(userId: String): User? {
        delay(1000)
        return User(id = userId, name = "Mock User")
    }
}
