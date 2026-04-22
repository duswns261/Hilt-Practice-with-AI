package com.cret.hilt_practice.data.repository

import com.cret.hilt_practice.data.model.User

interface UserRepository {
    suspend fun getUser(userId: String): User?
}
