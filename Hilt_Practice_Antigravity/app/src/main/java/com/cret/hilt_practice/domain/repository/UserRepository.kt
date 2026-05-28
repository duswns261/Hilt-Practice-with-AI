package com.cret.hilt_practice.domain.repository

import com.cret.hilt_practice.domain.model.User

interface UserRepository {
    suspend fun getUser(userId: String): Result<User>
}
