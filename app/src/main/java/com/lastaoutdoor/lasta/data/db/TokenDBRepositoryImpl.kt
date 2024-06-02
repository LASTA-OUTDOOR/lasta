package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.repository.db.TokenDBRepository
import kotlinx.coroutines.tasks.await

/**
 * TokenDBRepositoryImpl is a class that provides methods to upload, retrieve, and delete user
 * tokens in a Firebase Firestore database.
 *
 * @param context The application context, used to access resources.
 * @param database The FirebaseFirestore instance to interact with Firestore.
 * @constructor Creates an instance of TokenDBRepositoryImpl with the specified context and
 *   Firestore database.
 * @property tokenCollection The Firestore collection reference where user tokens are stored.
 */
class TokenDBRepositoryImpl(context: Context, database: FirebaseFirestore) : TokenDBRepository {

  // Reference to the Firestore collection where tokens are stored
  private val tokenCollection = database.collection(context.getString(R.string.token_db_name))

  /**
   * Uploads a user token to the Firestore database. If the token or userId is empty, the method
   * returns without performing any operation.
   *
   * @param userId The ID of the user.
   * @param token The token to be uploaded.
   */
  override fun uploadUserToken(userId: String, token: String) {
    if (token.isEmpty()) return
    if (userId.isEmpty()) return

    // Upload user token to database
    val document = tokenCollection.document(userId)
    val data = hashMapOf("token" to token, "timestamp" to FieldValue.serverTimestamp())
    document.set(data, SetOptions.merge())
  }

  /**
   * Retrieves a user token from the Firestore database by user ID. If the userId is empty or the
   * document does not exist, the method returns null.
   *
   * @param userId The ID of the user whose token is to be retrieved.
   * @return The token if it exists, or null if it does not.
   */
  override suspend fun getUserTokenById(userId: String): String? {
    if (userId.isEmpty()) return null

    // Get user token from database
    val document = tokenCollection.document(userId).get().await()
    if (!document.exists()) return null
    return document.getString("token")
  }

  /**
   * Deletes a user token from the Firestore database. If the userId is empty, the method returns
   * without performing any operation.
   *
   * @param userId The ID of the user whose token is to be deleted.
   */
  override fun deleteUserToken(userId: String) {
    if (userId.isEmpty()) return

    // Delete user token from database
    tokenCollection.document(userId).delete()
  }
}
