package com.chat.app.chats.domain.models

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Message(
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    val formattedTime: String
        get() = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(timestamp))
}
