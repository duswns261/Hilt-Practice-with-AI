package com.cret.hilt_practice.domain.usecase

import com.cret.hilt_practice.domain.model.UserProfile
import com.cret.hilt_practice.domain.repository.UserRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): UserProfile {
        return userRepository.getUser(userId)
    }
}
