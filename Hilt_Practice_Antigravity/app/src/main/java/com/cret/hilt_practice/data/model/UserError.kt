package com.cret.hilt_practice.data.model

import java.io.IOException

sealed class UserError {
    data object NotFound : UserError()
    data object Network : UserError()
    data object Unknown : UserError()

    companion object {
        fun from(throwable: Throwable): UserError = when (throwable) {
            is IOException -> Network
            else -> Unknown
        }
    }
}
