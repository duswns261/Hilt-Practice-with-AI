package com.cret.hilt_practice.domain.repository

import com.cret.hilt_practice.domain.model.UserProfile

interface UserRepository {
    suspend fun getUser(userId: String): UserProfile
}
