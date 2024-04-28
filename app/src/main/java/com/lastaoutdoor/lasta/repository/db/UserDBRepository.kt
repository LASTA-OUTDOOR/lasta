package com.lastaoutdoor.lasta.repository.db

import com.lastaoutdoor.lasta.models.user.UserModel

interface UserDBRepository {

  fun updateUser(user: UserModel)

  suspend fun getUserById(userId: String): UserModel?

  suspend fun getUserByEmail(email: String): UserModel?
}
