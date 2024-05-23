package com.lastaoutdoor.lasta.viewmodel.repo

import com.lastaoutdoor.lasta.repository.db.TokenDBRepository

class FakeTokenDBRepo : TokenDBRepository {

  var shouldThrowException = false

  override fun uploadUserToken(userId: String, token: String) {
    if (shouldThrowException) {
      throw Exception("FakeTokenDBRepo: uploadUserToken failed")
    }
  }

  override suspend fun getUserTokenById(userId: String): String? {
    if (shouldThrowException) {
      throw Exception("FakeTokenDBRepo: getUserTokenById failed")
    }
    return "fakeToken"
  }

  override fun deleteUserToken(userId: String) {
    if (shouldThrowException) {
      throw Exception("FakeTokenDBRepo: deleteUserToken failed")
    }
  }
}
