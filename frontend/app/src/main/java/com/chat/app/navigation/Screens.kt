package com.chat.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screens {
    @Serializable
    data class HomeScreen(val name: String, val email: String) : Screens()
    @Serializable
    data object LoginScreen : Screens()
    @Serializable
    data object SignUpScreen : Screens()
    @Serializable
    data class ChatScreen(val receiverName: String, val senderEmail: String, val receiverEmail: String) : Screens()
}