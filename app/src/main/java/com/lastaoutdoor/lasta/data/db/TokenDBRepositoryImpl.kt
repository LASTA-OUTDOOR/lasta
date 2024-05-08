package com.lastaoutdoor.lasta.data.db

import com.lastaoutdoor.lasta.repository.db.TokenDBRepository

class TokenDBRepositoryImpl : TokenDBRepository {
    override fun uploadUserToken(userId: String, token: String) {

    }

    override fun getUserTokenById(userId: String): String? {
        TODO("Not yet implemented")
    }

    override fun getTokensByIds(userIds: List<String>): List<String> {
        TODO("Not yet implemented")
    }
}