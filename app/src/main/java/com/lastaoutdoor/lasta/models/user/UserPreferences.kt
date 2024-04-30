package com.lastaoutdoor.lasta.models.user

data class UserPreferences(
    val isLoggedIn: Boolean,
    val user: UserModel = UserModel(""),
    val downloadedActivities: List<String> = emptyList()
)
