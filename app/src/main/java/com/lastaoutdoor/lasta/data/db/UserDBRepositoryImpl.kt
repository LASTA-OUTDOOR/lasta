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

@Suppress("UNCHECKED_CAST")
@Singleton
class UserDBRepositoryImpl @Inject constructor(context: Context, database: FirebaseFirestore) :
    UserDBRepository {

  private val userCollection = database.collection(context.getString(R.string.user_db_name))

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

  // Helper function to convert a document to a UserModel
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

  override suspend fun getUserById(userId: String): UserModel? {
    val user = userCollection.document(userId).get().await()
    return if (user.exists()) {
      documentToUserModel(user)
    } else null
  }

  override suspend fun getUserByEmail(email: String): UserModel? {
    val query = userCollection.whereEqualTo("email", email).get().await()
    return if (!query.isEmpty) {
      val user = query.documents[0]
      documentToUserModel(user)
    } else null
  }

  override suspend fun updateField(userId: String, field: String, value: Any) {
    val userDocumentRef = userCollection.document(userId)
    val data = hashMapOf(field to value)
    userDocumentRef.update(data as Map<String, Any>).await()
  }

  override suspend fun addFavorite(userId: String, activityId: String) {
    val userDocumentRef = userCollection.document(userId)
    userDocumentRef.update("favorites", FieldValue.arrayUnion(activityId)).await()
  }

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

  override suspend fun removeFavorite(userId: String, activityId: String) {
    val userDocumentRef = userCollection.document(userId)
    userDocumentRef.update("favorites", FieldValue.arrayRemove(activityId)).await()
  }

  override suspend fun deleteUser(userId: String) {
    userCollection.document(userId).delete().await()

    // Delete friends from other users
    val query = userCollection.whereArrayContains("friends", userId).get().await()
    query.documents.forEach { doc ->
      val friends = doc.get("friends") as List<String>
      userCollection.document(doc.id).update("friends", friends.filter { it != userId }).await()
    }
  }
}
