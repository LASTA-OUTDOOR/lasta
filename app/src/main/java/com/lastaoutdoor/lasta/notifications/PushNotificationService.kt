package com.lastaoutdoor.lasta.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService : FirebaseMessagingService() {

  override fun onNewToken(token: String) {
    super.onNewToken(token)
    // Update server (call db)

  }

  override fun onMessageReceived(message: RemoteMessage) {
    super.onMessageReceived(message)
  }
}
