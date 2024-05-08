package com.lastaoutdoor.lasta.notifications

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService : FirebaseMessagingService() {

  override fun onNewToken(token: String) {
    super.onNewToken(token)
    // Update server (call db)
    val deviceToken = hashMapOf(
      "token" to token,
      "timestamp" to FieldValue.serverTimestamp(),
    )
    // Get user ID from Firebase Auth or your own server
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    Firebase.firestore.collection("user_fcm_tokens").document(userId)
      .set(deviceToken)
  }

  override fun onMessageReceived(message: RemoteMessage) {
    super.onMessageReceived(message)
  }
}
