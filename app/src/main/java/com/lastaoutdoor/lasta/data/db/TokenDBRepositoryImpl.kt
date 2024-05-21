package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.repository.db.TokenDBRepository
import kotlinx.coroutines.tasks.await

class TokenDBRepositoryImpl(context: Context, database: FirebaseFirestore) : TokenDBRepository {
  private val tokenCollection = database.collection(context.getString(R.string.token_db_name))

  override fun uploadUserToken(userId: String, token: String) {
    if (token.isEmpty()) return
    if (userId.isEmpty()) return
    // Upload user token to database
    val document = tokenCollection.document(userId)
    val data = hashMapOf("token" to token, "timestamp" to FieldValue.serverTimestamp())
    document.set(data, SetOptions.merge())
  }

  override suspend fun getUserTokenById(userId: String): String? {
    if (userId.isEmpty()) return null
    // Get user token from database
    val document = tokenCollection.document(userId).get().await()
    if (!document.exists()) return null
    return document.getString("token")
  }

  // override suspend fun getTokensByIds(userIds: List<String>): List<String> {}

  override fun deleteUserToken(userId: String) {
        if (userId.isEmpty()) return
        // Delete user token from database
        tokenCollection.document(userId).delete()
    }
}
