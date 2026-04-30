package com.cret.hilt_practice.data.repository

import com.cret.hilt_practice.data.model.User
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor() : UserRepository {
    override suspend fun getUser(userId: String): Result<User> {
        return try {
            delay(1000)
            Result.success(User(id = userId, name = "Mock User"))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
