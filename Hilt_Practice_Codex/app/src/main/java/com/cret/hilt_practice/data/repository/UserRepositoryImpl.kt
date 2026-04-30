package com.cret.hilt_practice.data.repository

import com.cret.hilt_practice.data.model.User
import com.cret.hilt_practice.data.source.UserDataSource
import com.cret.hilt_practice.domain.model.UserProfile
import com.cret.hilt_practice.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun getUser(userId: String): UserProfile {
        return userDataSource.getUser(userId).toDomain()
    }

    private fun User.toDomain(): UserProfile {
        return UserProfile(
            id = id,
            name = name,
            email = email
        )
    }
}
