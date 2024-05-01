package com.lastaoutdoor.lasta.data.db

import android.content.Context
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
    userCollection
        .document(user.userId)
        .set(userData, SetOptions.merge())
        .addOnSuccessListener { /* TODO */}
        .addOnFailureListener { e -> e.printStackTrace() }
  }

  override suspend fun getUserById(userId: String): UserModel? {
    val user = userCollection.document(userId).get().await()
    return if (user.exists()) {
      val userName = user.getString("userName") ?: ""
      val email = user.getString("email") ?: ""
      val profilePictureUrl = user.getString("profilePictureUrl") ?: ""
      val description = user.getString("description") ?: ""
      val language = Language.valueOf(user.getString("language") ?: Language.ENGLISH.name)
      val prefActivity =
          ActivityType.valueOf(user.getString("prefActivity") ?: ActivityType.CLIMBING.name)
      val levels =
          UserActivitiesLevel(
              climbingLevel =
                  UserLevel.valueOf(user.getString("climbingLevel") ?: UserLevel.BEGINNER.name),
              hikingLevel =
                  UserLevel.valueOf(user.getString("hikingLevel") ?: UserLevel.BEGINNER.name),
              bikingLevel =
                  UserLevel.valueOf(user.getString("bikingLevel") ?: UserLevel.BEGINNER.name))
      val friends = (user.get("friends") ?: emptyList<String>()) as List<String>
      val friendRequests = (user.get("friendRequests") ?: emptyList<String>()) as List<String>
      val favorites = (user.get("favorites") ?: emptyList<String>()) as List<String>
      UserModel(
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
    } else null
  }

  override suspend fun getUserByEmail(email: String): UserModel? {
    val query = userCollection.whereEqualTo("email", email).get().await()
    return if (!query.isEmpty) {
      val user = query.documents[0]
      val userId = user.id
      val userName = user.getString("userName") ?: ""
      val profilePictureUrl = user.getString("profilePictureUrl") ?: ""
      val description = user.getString("description") ?: ""
      val language = Language.valueOf(user.getString("language") ?: Language.ENGLISH.name)
      val prefActivity =
          ActivityType.valueOf(user.getString("prefActivity") ?: ActivityType.CLIMBING.name)
      val levels =
          UserActivitiesLevel(
              climbingLevel =
                  UserLevel.valueOf(user.getString("climbingLevel") ?: UserLevel.BEGINNER.name),
              hikingLevel =
                  UserLevel.valueOf(user.getString("hikingLevel") ?: UserLevel.BEGINNER.name),
              bikingLevel =
                  UserLevel.valueOf(user.getString("bikingLevel") ?: UserLevel.BEGINNER.name))
      val friends = (user.get("friends") ?: emptyList<String>()) as List<String>
      val friendRequests = (user.get("friendRequests") ?: emptyList<String>()) as List<String>
      val favorites = (user.get("favorites") ?: emptyList<String>()) as List<String>
      UserModel(
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
    } else null
  }

  override suspend fun updateField(userId: String, field: String, value: Any) {
    // Create a reference to the document with the user's UID
    val userDocumentRef = userCollection.document(userId)

    // Create a data map with the field and value
    val data = hashMapOf(field to value)

    // Update the field in the document
    userDocumentRef.update(data as Map<String, Any>).await()
  }

  override suspend fun addFavorite(userId: String, activityId: String) {
    val userDocumentRef = userCollection.document(userId)
    userDocumentRef.update("favorites", FieldValue.arrayUnion(activityId)).await()
  }

  override suspend fun removeFavorite(userId: String, activityId: String) {
    val userDocumentRef = userCollection.document(userId)
    userDocumentRef.update("favorites", FieldValue.arrayRemove(activityId)).await()
  }
}
