package com.lastaoutdoor.lasta.models.notifications

/**
 * Data class for sending a message to a specific user
 * Dto means data transfer object
 */
data class SendMessageDto(val to: String?, val notification: NotificationBody)

data class NotificationBody(val title: String, val body: String)
