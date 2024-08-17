package com.chat.app.chats.domain.repository

import com.chat.app.chats.domain.models.Message
import com.chat.app.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface ChatRepository {
    fun connect()
    fun disconnect()
    suspend fun sendMessage(senderId: String, receiverId: String, message: String): Resource<Unit>
    val messagesFlow: SharedFlow<Resource<Message>>
    suspend fun getMessages(senderId: String, receiverId: String): Flow<Resource<List<Message>>>
    fun saveToDatabase(message: Message)
    suspend fun getLatestMessage(senderId: String, receiverId: String): Flow<Resource<Message>>
}
