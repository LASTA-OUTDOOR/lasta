package com.lastaoutdoor.lasta.repository.db

import com.lastaoutdoor.lasta.models.user.UserModel

/** Repository for handling user data in the database. */
interface UserDBRepository {

  /**
   * Updates the user data in the database. If the user does not exist, it will also be created.
   *
   * @param user the user data to update
   */
  fun updateUser(user: UserModel)

  /**
   * Retrieves the user data from the database by the user ID.
   *
   * @param userId the user ID to retrieve the data
   * @return the user data if it exists, null otherwise
   */
  suspend fun getUserById(userId: String): UserModel?

  /**
   * Retrieves the user data from the database by the email.
   *
   * @param email the email to retrieve the data
   * @return the user data if it exists, null otherwise
   */
  suspend fun getUserByEmail(email: String): UserModel?

  /**
   * Updates the field of the user data in the database.
   *
   * @param userId the user ID to update the field
   * @param field the field to update
   * @param value the value to update
   */
  suspend fun updateField(userId: String, field: String, value: Any)

  /**
   * Adds an activity to the user's favorites.
   *
   * @param userId the user ID to add the favorite
   * @param activityId the activity ID to add to the favorite
   */
  suspend fun addFavorite(userId: String, activityId: String)

  /**
   * Removes an activity from the user's favorites.
   *
   * @param userId the user ID to remove the favorite
   * @param activityId the activity ID to remove from the favorite
   */
  suspend fun removeFavorite(userId: String, activityId: String)

  /**
   * Delete the user data from the database.
   *
   * @param userId the user ID to delete the data
   */
  suspend fun deleteUser(userId: String)
}
