package com.lastaoutdoor.lasta.data.model.user

data class UserPreferences(
    val isLoggedIn: Boolean,
    val uid: String,
    val userName: String,
    val email: String,
    val profilePictureUrl: String,
    val hikingLevel: HikingLevel
)