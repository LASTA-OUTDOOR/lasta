package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.lastaoutdoor.lasta.repository.db.TokenDBRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TokenDBRepositoryImpl @Inject constructor(context: Context, database: FirebaseFirestore) :
    TokenDBRepository {
  private val tokenCollection = database.collection("user_fcm_tokens")

  override suspend fun uploadUserToken(userId: String, token: String) {
    if (token.isEmpty()) return
    if (userId.isEmpty()) return
    // Upload user token to database
    val document = tokenCollection.document(userId)
    val data = hashMapOf("token" to token, "createdAt" to FieldValue.serverTimestamp())
    document.set(data).await()
  }

  override suspend fun getUserTokenById(userId: String): String? {
    if (userId.isEmpty()) return null
    // Get user token from database
    val document = tokenCollection.document(userId).get().await()
    return document.getString("token")
  }

  override suspend fun getTokensByIds(userIds: List<String>): List<String> {
    TODO("Not yet implemented")
  }
}
