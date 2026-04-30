package com.cret.hilt_practice.domain.error

class UserNotFoundException(
    val userId: String
) : Exception("User not found: $userId")
