package com.chat.app.users.domain.models

import com.chat.app.chats.domain.models.Message
import com.chat.app.core.utils.Resource

data class SignUpScreenState<out T>(
    val name: String = "",
    val email: String = "",
    val nameError: String = "",
    val emailError: String = "",
    val state: Resource<T> = Resource.Idle,
    val users: List<User> = emptyList(),
    val isLoggedIn: Boolean = false,
    val message: Message = Message()
)
