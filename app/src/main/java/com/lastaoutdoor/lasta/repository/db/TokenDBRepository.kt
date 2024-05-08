package com.lastaoutdoor.lasta.repository.db

interface TokenDBRepository {

    /**
     * Upload user token to database
     */
    fun uploadUserToken(userId: String, token: String)

    /**
     * Get user token from database
     */
    fun getUserTokenById(userId: String): String?

    /**
     * Get multiple tokens from database for topics
     */
    fun getTokensByIds(userIds : List<String>): List<String>


}