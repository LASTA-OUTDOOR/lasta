package com.lastaoutdoor.lasta.data.model.user

data class UserPreferences(val isLoggedIn: Boolean, val user: UserModel = UserModel(""))
