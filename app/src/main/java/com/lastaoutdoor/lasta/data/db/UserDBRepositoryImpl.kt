package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.db.UserDBRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

/**
 * Implementation of the [UserDBRepository] interface, managing user data in a Firestore database.
 *
 * @param context The application context.
 * @param database The Firebase Firestore database instance.
 * @constructor Creates an instance of [UserDBRepositoryImpl] with the provided context and
 *   Firestore database instance.
 */
@Suppress("UNCHECKED_CAST")
@Singleton
class UserDBRepositoryImpl @Inject constructor(context: Context, database: FirebaseFirestore) :
    UserDBRepository {

  // Reference to the Firestore collection for users
  private val userCollection = database.collection(context.getString(R.string.user_db_name))

  /**
   * Updates the user's information in the Firestore database.
   *
   * @param user The [UserModel] object containing updated user information.
   */
  override fun updateUser(user: UserModel) {
    val userData =
        hashMapOf(
            "userName" to user.userName,
            "email" to user.email,
            "profilePictureUrl" to user.profilePictureUrl,
            "description" to user.description,
            "language" to user.language.name,
            "prefActivity" to user.prefActivity.name,
            "levels" to
                hashMapOf(
                    "climbingLevel" to user.levels.climbingLevel.name,
                    "hikingLevel" to user.levels.hikingLevel.name,
                    "bikingLevel" to user.levels.bikingLevel.name),
            "friends" to user.friends,
            "friendRequests" to user.friendRequests,
            "favorites" to user.favorites)
    userCollection.document(user.userId).set(userData, SetOptions.merge())
  }

  /**
   * Helper function to convert a Firestore [DocumentSnapshot] to a [UserModel].
   *
   * @param document The Firestore document snapshot.
   * @return The converted [UserModel].
   */
  private fun documentToUserModel(document: DocumentSnapshot): UserModel {
    val userId = document.id
    val userName = document.getString("userName") ?: ""
    val email = document.getString("email") ?: ""
    val profilePictureUrl = document.getString("profilePictureUrl") ?: ""
    val description = document.getString("description") ?: ""
    val language = Language.valueOf(document.getString("language") ?: Language.ENGLISH.name)
    val prefActivity =
        ActivityType.valueOf(document.getString("prefActivity") ?: ActivityType.CLIMBING.name)
    val levelsMap = (document.get("levels") ?: HashMap<String, String>()) as Map<String, String>
    val levels =
        UserActivitiesLevel(
            climbingLevel =
                UserLevel.valueOf(levelsMap["climbingLevel"] ?: UserLevel.BEGINNER.name),
            hikingLevel = UserLevel.valueOf(levelsMap["hikingLevel"] ?: UserLevel.BEGINNER.name),
            bikingLevel = UserLevel.valueOf(levelsMap["bikingLevel"] ?: UserLevel.BEGINNER.name))
    val friends = (document.get("friends") ?: emptyList<String>()) as List<String>
    val friendRequests = (document.get("friendRequests") ?: emptyList<String>()) as List<String>
    val favorites = (document.get("favorites") ?: emptyList<String>()) as List<String>
    return UserModel(
        userId,
        userName,
        email,
        profilePictureUrl,
        description,
        language,
        prefActivity,
        levels,
        friends,
        friendRequests,
        favorites)
  }

  /**
   * Retrieves a user by their ID from the Firestore database.
   *
   * @param userId The ID of the user to retrieve.
   * @return The [UserModel] object if the user exists, otherwise null.
   */
  override suspend fun getUserById(userId: String): UserModel? {
    val user = userCollection.document(userId).get().await()
    return if (user.exists()) {
      documentToUserModel(user)
    } else null
  }

  /**
   * Retrieves a user by their email from the Firestore database.
   *
   * @param email The email of the user to retrieve.
   * @return The [UserModel] object if the user exists, otherwise null.
   */
  override suspend fun getUserByEmail(email: String): UserModel? {
    val query = userCollection.whereEqualTo("email", email).get().await()
    return if (!query.isEmpty) {
      val user = query.documents[0]
      documentToUserModel(user)
    } else null
  }

  /**
   * Updates a specific field for a user in the Firestore database.
   *
   * @param userId The ID of the user.
   * @param field The field to update.
   * @param value The new value for the field.
   */
  override suspend fun updateField(userId: String, field: String, value: Any) {
    val userDocumentRef = userCollection.document(userId)
    val data = hashMapOf(field to value)
    userDocumentRef.update(data as Map<String, Any>).await()
  }

  /**
   * Adds a favorite activity to a user's list of favorites in the Firestore database.
   *
   * @param userId The ID of the user.
   * @param activityId The ID of the activity to add to favorites.
   */
  override suspend fun addFavorite(userId: String, activityId: String) {
    val userDocumentRef = userCollection.document(userId)
    userDocumentRef.update("favorites", FieldValue.arrayUnion(activityId)).await()
  }

  /**
   * Searches for users by a username substring that are not the current user or their friends.
   *
   * @param query The substring to search for in usernames.
   * @param friends The list of the current user's friends.
   * @param user The current user.
   * @return A list of [UserModel] objects matching the search criteria.
   */
  override suspend fun getUsersByUsernameWithSubstring(
      query: String,
      friends: List<UserModel>,
      user: UserModel
  ): List<UserModel> {
    // if the query is empty, return an empty list
    if (query.isEmpty() || query.isBlank()) {
      return emptyList()
    }
    // get all users that are not the current user or his/her friends
    val allUsersUsernames =
        userCollection.whereNotIn("email", friends.map { it.email }.plus(user.email)).get().await()
    if (allUsersUsernames.isEmpty) {
      return emptyList()
    }
    val usersToList = mutableListOf<UserModel>()
    // for each user, check if the username contains the query
    allUsersUsernames.documents.forEach { doc ->
      // get the username of the user
      val userName = doc.getString("userName") ?: ""
      // if the username contains the query, return the user
      if (userName.lowercase().contains(query.lowercase())) {
        usersToList.add(documentToUserModel(doc))
      }
    }
    return usersToList
  }

  /**
   * Removes a favorite activity from a user's list of favorites in the Firestore database.
   *
   * @param userId The ID of the user.
   * @param activityId The ID of the activity to remove from favorites.
   */
  override suspend fun removeFavorite(userId: String, activityId: String) {
    val userDocumentRef = userCollection.document(userId)
    userDocumentRef.update("favorites", FieldValue.arrayRemove(activityId)).await()
  }

  /**
   * Deletes a user from the Firestore database and removes the user from their friends' lists.
   *
   * @param userId The ID of the user to delete.
   */
  override suspend fun deleteUser(userId: String) {
    userCollection.document(userId).delete()

    // Delete friends from other users
    val query = userCollection.whereArrayContains("friends", userId).get().await()
    query.documents.forEach { doc ->
      val friends = doc.get("friends") as List<String>
      userCollection.document(doc.id).update("friends", friends.filter { it != userId }).await()
    }
  }
}
