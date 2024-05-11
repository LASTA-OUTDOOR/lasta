package com.lastaoutdoor.lasta.models.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserPreferences(
    val isLoggedIn: Boolean,
    @PrimaryKey val user: UserModel = UserModel(""),
    val downloadedActivities: List<String> = emptyList()
)
