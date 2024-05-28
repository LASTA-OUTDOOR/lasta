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
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

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
      val levelsMap = (user.get("levels") ?: HashMap<String, String>()) as Map<String, String>
      val levels =
          UserActivitiesLevel(
              climbingLevel =
                  UserLevel.valueOf(levelsMap["climbingLevel"] ?: UserLevel.BEGINNER.name),
              hikingLevel = UserLevel.valueOf(levelsMap["hikingLevel"] ?: UserLevel.BEGINNER.name),
              bikingLevel = UserLevel.valueOf(levelsMap["bikingLevel"] ?: UserLevel.BEGINNER.name))
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
      val levelsMap = (user.get("levels") ?: HashMap<String, String>()) as Map<String, String>
      val levels =
          UserActivitiesLevel(
              climbingLevel =
                  UserLevel.valueOf(levelsMap["climbingLevel"] ?: UserLevel.BEGINNER.name),
              hikingLevel = UserLevel.valueOf(levelsMap["hikingLevel"] ?: UserLevel.BEGINNER.name),
              bikingLevel = UserLevel.valueOf(levelsMap["bikingLevel"] ?: UserLevel.BEGINNER.name))
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
    val userDocumentRef = userCollection.document(userId)
    val data = hashMapOf(field to value)
    userDocumentRef.update(data as Map<String, Any>).await()
  }

  override suspend fun addFavorite(userId: String, activityId: String) {
    val userDocumentRef = userCollection.document(userId)
    userDocumentRef.update("favorites", FieldValue.arrayUnion(activityId)).await()
  }

  override suspend fun getUsersByUsernameWithSubstring(query: String): List<UserModel> {
    val allUsersUsernames = userCollection.get().await()
    allUsersUsernames.documents.forEach { doc ->
      // get the username of the user
      val userName = doc.getString("userName") ?: ""
        println("User name: $userName")
      // if the username contains the query, return the user
      if (userName.contains(query)) {
          println("User name corres: $userName")
        return listOf(
            UserModel(
                userId = doc.id,
                userName = userName,
                email = doc.getString("email") ?: "",
                profilePictureUrl = doc.getString("profilePictureUrl") ?: "",
                description = doc.getString("description") ?: "",
                language = Language.valueOf(doc.getString("language") ?: Language.ENGLISH.name),
                prefActivity =
                    ActivityType.valueOf(
                        doc.getString("prefActivity") ?: ActivityType.CLIMBING.name),
                levels =
                    UserActivitiesLevel(
                        climbingLevel =
                            UserLevel.valueOf(
                                doc.getString("climbingLevel") ?: UserLevel.BEGINNER.name),
                        hikingLevel =
                            UserLevel.valueOf(
                                doc.getString("hikingLevel") ?: UserLevel.BEGINNER.name),
                        bikingLevel =
                            UserLevel.valueOf(
                                doc.getString("bikingLevel") ?: UserLevel.BEGINNER.name)),
                friends = (doc.get("friends") ?: emptyList<String>()) as List<String>,
                friendRequests = (doc.get("friendRequests") ?: emptyList<String>()) as List<String>,
                favorites = (doc.get("favorites") ?: emptyList<String>()) as List<String>))
      }
    }
    return emptyList()
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
