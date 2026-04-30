package com.cret.hilt_practice.data.source

import com.cret.hilt_practice.data.model.User

interface UserDataSource {
    suspend fun getUser(userId: String): User
}
