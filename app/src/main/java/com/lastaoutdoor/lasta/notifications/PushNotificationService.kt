package com.lastaoutdoor.lasta.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("New token: $token")
        // Update server
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        println("Received message: ${message.data.toMap()}")
        // Respond to received messages
    }
}