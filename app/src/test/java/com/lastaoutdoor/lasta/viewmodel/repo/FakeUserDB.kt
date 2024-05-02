package com.lastaoutdoor.lasta.viewmodel.repo

import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.db.UserDBRepository

class FakeUserDB : UserDBRepository {
  val fakeUserModel = UserModel(userId = "id")

  override fun updateUser(user: UserModel) {}

  override suspend fun getUserById(userId: String): UserModel? {
    return fakeUserModel
  }

  override suspend fun getUserByEmail(email: String): UserModel? {
    return fakeUserModel
  }

  override suspend fun updateField(userId: String, field: String, value: Any) {}
}
