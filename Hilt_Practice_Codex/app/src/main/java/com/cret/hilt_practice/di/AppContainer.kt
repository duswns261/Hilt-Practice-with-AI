package com.cret.hilt_practice.di

import com.cret.hilt_practice.data.repository.FakeUserRepository
import com.cret.hilt_practice.data.repository.UserRepository

class AppContainer {
    val userRepository: UserRepository = FakeUserRepository()
}
