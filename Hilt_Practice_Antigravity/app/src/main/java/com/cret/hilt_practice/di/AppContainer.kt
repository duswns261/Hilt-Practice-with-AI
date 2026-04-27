package com.cret.hilt_practice.di

import com.cret.hilt_practice.data.repository.UserRepository
import com.cret.hilt_practice.data.repository.UserRepositoryImpl

class AppContainer {
    val repository: UserRepository = UserRepositoryImpl()
}
