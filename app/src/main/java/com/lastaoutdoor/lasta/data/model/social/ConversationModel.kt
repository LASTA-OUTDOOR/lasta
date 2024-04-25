package com.lastaoutdoor.lasta.data.model.social

import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.data.model.user.UserModel

// This class represents a message
data class MessageModel(
    val from: String, // user id of the sender -> more useful in group chats
    val content: String,
    val timestamp: Timestamp
)

// This class represents a conversation
data class ConversationModel(
    val users:
        List<UserModel>, // array of users in the conversation -> will be easier to expand to group
    // chats
    val messages: List<MessageModel>,
    val lastMessage:
        MessageModel? // Useful if we want to preview the last message in the conversation
)
