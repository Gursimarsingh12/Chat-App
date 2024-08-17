package com.chat.app.chats.data.repository

import android.util.Log
import com.chat.app.chats.domain.models.Message
import com.chat.app.chats.domain.repository.ChatRepository
import com.chat.app.core.utils.Resource
import com.google.firebase.database.DatabaseReference
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

class ChatRepositoryImpl(
    private val socket: Socket,
    private val db: DatabaseReference,
) : ChatRepository {

    private val _messagesFlow = MutableSharedFlow<Resource<Message>>(extraBufferCapacity = 1)
    override val messagesFlow: SharedFlow<Resource<Message>> = _messagesFlow

    override fun connect() {
        socket.connect()
        socket.on(Socket.EVENT_CONNECT, onConnect)
        socket.on("message", onMessageReceivedCallback)
    }

    private val onConnect = Emitter.Listener {
        Log.d("Socket", "Connected to Socket.IO")
    }

    private val onMessageReceivedCallback = Emitter.Listener { args ->
        if (args.isNotEmpty()) {
            try {
                val data = args[0] as JSONObject
                val message = Message(
                    text = data.getString("message"),
                    senderId = data.getString("senderId"),
                    receiverId = data.getString("receiverId")
                )
                _messagesFlow.tryEmit(Resource.Success(message))
                Log.d("Socket", "Message received: $message")
            } catch (e: JSONException) {
                _messagesFlow.tryEmit(Resource.Error(e.localizedMessage))
                Log.e("Socket", "JSON parsing error: ${e.message}")
            }
        }
    }

    private fun emailForFirebase(email: String): String {
        return email.replace(".", ",")
    }

    override suspend fun sendMessage(
        senderId: String,
        receiverId: String,
        message: String,
    ): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val jsonObject = JSONObject().apply {
                    put("senderId", senderId)
                    put("receiverId", receiverId)
                    put("message", message)
                }
                socket.emit("sendMessage", jsonObject)
                Log.d("Socket", "Message sent: $message")
                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage)
            }
        }
    }

    override fun disconnect() {
        socket.off(Socket.EVENT_CONNECT, onConnect)
        socket.off("message", onMessageReceivedCallback)
        socket.disconnect()
    }

    override suspend fun getMessages(
        senderId: String,
        receiverId: String
    ): Flow<Resource<List<Message>>> = flow {
        emit(Resource.Loading)
        try {
            val newSenderId = emailForFirebase(senderId)
            val newReceiverId = emailForFirebase(receiverId)
            val chatRef = db.child("messages").child("${newSenderId}_${newReceiverId}")
            val query = chatRef.orderByChild("timestamp").get().await()

            val messages = query.children.mapNotNull { snapshot ->
                snapshot.getValue(Message::class.java)
            }.filter { message ->
                (message.senderId == senderId && message.receiverId == receiverId) ||
                        (message.senderId == receiverId && message.receiverId == senderId)
            }

            Log.d("Database", "Messages: $messages")
            emit(Resource.Success(messages))
        } catch (e: Exception) {
            Log.e("Database", "Failed to get messages: ${e.localizedMessage}")
            emit(Resource.Error(e.localizedMessage))
        }
    }

    override fun saveToDatabase(message: Message) {
        val sanitizedSenderId = emailForFirebase(message.senderId)
        val sanitizedReceiverId = emailForFirebase(message.receiverId)
        val chatRefSenderToReceiver = db.child("messages").child("${sanitizedSenderId}_${sanitizedReceiverId}")
        val chatRefReceiverToSender = db.child("messages").child("${sanitizedReceiverId}_${sanitizedSenderId}")
        val messageId = chatRefSenderToReceiver.push().key
        if (messageId != null) {
            val messageEntry = chatRefSenderToReceiver.child(messageId)
            messageEntry.setValue(message)
                .addOnSuccessListener {
                    Log.d("Database", "Message saved to database for sender")
                }
                .addOnFailureListener {
                    Log.e("Database", "Failed to save message to database for sender: ${it.message}")
                }
            chatRefReceiverToSender.child(messageId).setValue(message)
                .addOnSuccessListener {
                    Log.d("Database", "Message saved to database for receiver")
                }
                .addOnFailureListener {
                    Log.e("Database", "Failed to save message to database for receiver: ${it.message}")
                }
        } else {
            Log.e("Database", "Failed to generate message ID")
        }
    }

    override suspend fun getLatestMessage(senderId: String, receiverId: String): Flow<Resource<Message>> = flow {
        emit(Resource.Loading)
        try {
            val newSenderId = emailForFirebase(senderId)
            val newReceiverId = emailForFirebase(receiverId)
            val chatRef = db.child("messages").child("${newSenderId}_${newReceiverId}")

            val query = chatRef.orderByChild("timestamp").limitToLast(1).get().await()

            val message = query.children.mapNotNull { snapshot ->
                snapshot.getValue(Message::class.java)
            }.lastOrNull()

            if (message != null) {
                emit(Resource.Success(message))
            } else {
                emit(Resource.Error("No messages found"))
            }
        } catch (e: Exception) {
            Log.e("Database", "Failed to get latest message: ${e.localizedMessage}")
            emit(Resource.Error(e.localizedMessage))
        }
    }



}

