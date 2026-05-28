package com.cret.hilt_practice.domain.usecase

import com.cret.hilt_practice.domain.model.User
import com.cret.hilt_practice.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String): Result<User> =
        repository.getUser(userId)
}
