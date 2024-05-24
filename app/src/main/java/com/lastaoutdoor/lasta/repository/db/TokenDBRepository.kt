package com.lastaoutdoor.lasta.repository.db

interface TokenDBRepository {

  /** Upload user token to database */
  fun uploadUserToken(userId: String, token: String)

  /** Get user token from database */
  suspend fun getUserTokenById(userId: String): String?

  /** Get multiple tokens from database for topics */
  // suspend fun getTokensByIds(userIds: List<String>): List<String>

  /**
   * Delete user token from database
   *
   * @param userId The ID of the user.
   */
  fun deleteUserToken(userId: String)
}
