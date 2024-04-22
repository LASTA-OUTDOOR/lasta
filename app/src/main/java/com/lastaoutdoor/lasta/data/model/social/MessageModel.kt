package com.lastaoutdoor.lasta.data.model.social

//This class represents a message
data class MessageModel(
    val messageId: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Long
)
