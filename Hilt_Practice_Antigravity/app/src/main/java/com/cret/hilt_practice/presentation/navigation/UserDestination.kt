package com.cret.hilt_practice.presentation.navigation

object UserDestination {
    const val ARG_USER_ID = "userId"
    const val ROUTE = "user/{$ARG_USER_ID}"

    fun createRoute(userId: String): String {
        return "user/$userId"
    }
}