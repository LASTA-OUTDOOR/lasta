package com.lastaoutdoor.lasta.viewmodel.repo

import com.lastaoutdoor.lasta.repository.db.TokenDBRepository

class FakeTokenDBRepo : TokenDBRepository {
  override fun uploadUserToken(userId: String, token: String) {}

  override suspend fun getUserTokenById(userId: String): String? {
    return "fakeToken"
  }
}
