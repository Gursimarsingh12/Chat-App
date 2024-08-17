package com.chat.app.chats.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.app.chats.data.repository.ChatRepositoryImpl
import com.chat.app.chats.domain.models.Message
import com.chat.app.chats.domain.repository.ChatRepository
import com.chat.app.core.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    init {
        chatRepository.connect()
    }

    fun getMessages(receiverId: String){
        viewModelScope.launch {
            chatRepository.messagesFlow.collect { state ->
                if (state is Resource.Success) {
                    _messages.value += state.data ?: Message()
                    if (state.data?.receiverId == receiverId){
                        chatRepository.saveToDatabase(state.data)
                    }
                }
            }
        }
    }

    fun sendMessage(senderId: String, receiverId: String, message: String) {
        viewModelScope.launch {
            chatRepository.sendMessage(senderId, receiverId, message)
        }
    }

    fun getAllPreviousMessages(senderId: String, receiverId: String) {
        viewModelScope.launch {
            chatRepository.getMessages(senderId, receiverId).collectLatest { state ->
                if (state is Resource.Success) {
                    _messages.value = state.data ?: emptyList()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatRepository.disconnect()
    }
}
