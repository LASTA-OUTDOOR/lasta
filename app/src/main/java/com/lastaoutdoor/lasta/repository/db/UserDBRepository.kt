package com.lastaoutdoor.lasta.repository.db

import com.lastaoutdoor.lasta.models.user.UserModel

interface UserDBRepository {

  fun updateUser(user: UserModel)

  suspend fun getUserById(userId: String): UserModel?

  suspend fun getUserByEmail(email: String): UserModel?

  suspend fun updateField(userId: String, field: String, value: Any)

  suspend fun addFavorite(userId: String, activityId: String)

  suspend fun removeFavorite(userId: String, activityId: String)
}
