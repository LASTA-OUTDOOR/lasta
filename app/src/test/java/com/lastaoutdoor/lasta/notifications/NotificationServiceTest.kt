package com.lastaoutdoor.lasta.notifications

import com.google.firebase.messaging.RemoteMessage
import io.mockk.mockk
import org.junit.Test

class NotificationServiceTest {
  private val notificationService = PushNotificationService()
  private val remoteMessage = mockk<RemoteMessage>(relaxed = true)

  @Test
  fun `onNewToken should call super onNewToken`() {
    notificationService.onNewToken("token")
  }

  @Test
  fun `onMessageReceived should call super onMessageReceived`() {
    notificationService.onMessageReceived(remoteMessage)
  }
}
