package com.lastaoutdoor.lasta.viewmodel.repo

import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.db.UserDBRepository

class FakeUserDB : UserDBRepository {
  val fakeUserModel = UserModel(userId = "id")

  var shouldThrowException = false

  override fun updateUser(user: UserModel) {
    if (shouldThrowException) {
      throw Exception("FakeUserDB: updateUser failed")
    }
  }

  override suspend fun getUserById(userId: String): UserModel? {
    if (shouldThrowException) {
      throw Exception("FakeUserDB: getUserById failed")
    }
    return UserModel(userId = userId)
  }

  override suspend fun getUserByEmail(email: String): UserModel? {
    if (shouldThrowException) {
      throw Exception("FakeUserDB: getUserByEmail failed")
    }
    return UserModel(userId = email)
  }

  override suspend fun updateField(userId: String, field: String, value: Any) {
    if (shouldThrowException) {
      throw Exception("FakeUserDB: updateField failed")
    }
  }

  override suspend fun addFavorite(userId: String, activityId: String) {
    if (shouldThrowException) {
      throw Exception("FakeUserDB: addFavorite failed")
    }
  }

  override suspend fun getUsersByUsernameWithSubstring(query: String): List<UserModel> {
    if (shouldThrowException) {
      throw Exception("FakeUserDB: getUsersByUsernameWithSubstring failed")
    }
    return listOf(fakeUserModel)
  }

  override suspend fun removeFavorite(userId: String, activityId: String) {
    if (shouldThrowException) {
      throw Exception("FakeUserDB: removeFavorite failed")
    }
  }

  override suspend fun deleteUser(userId: String) {
    if (shouldThrowException) {
      throw Exception("FakeUserDB: deleteUser failed")
    }
  }
}
