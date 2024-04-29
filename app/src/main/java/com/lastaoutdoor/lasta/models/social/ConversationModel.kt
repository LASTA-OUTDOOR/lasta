package com.lastaoutdoor.lasta.models.social

import com.google.firebase.Timestamp

// This class represents a message
data class MessageModel(
    val from: String, // user id of the sender -> more useful in group chats
    val content: String,
    val timestamp: Timestamp
)

// This class represents a conversation
data class ConversationModel(
    val members: List<String>,
    val messages: List<MessageModel>,
    val lastMessage:
        MessageModel? // Useful if we want to preview the last message in the conversation
)
