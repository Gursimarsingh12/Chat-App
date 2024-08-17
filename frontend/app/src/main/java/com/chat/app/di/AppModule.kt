package com.chat.app.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.chat.app.chats.data.repository.ChatRepositoryImpl
import com.chat.app.chats.domain.repository.ChatRepository
import com.chat.app.chats.presentation.viewmodels.ChatViewModel
import com.chat.app.shared_preferences.SharedPreferencesManager
import com.chat.app.users.data.repository.UsersRepositoryImpl
import com.chat.app.users.domain.repository.UsersRepository
import com.chat.app.users.domain.use_case.UsersUseCase
import com.chat.app.users.presentation.viewmodel.UsersViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import java.net.URISyntaxException

object AppModule {
    val appModule = module {
        single<Socket> {
            try {
                Log.d("Socket", "Socket created")
                IO.socket("https://chat-app-vueg.onrender.com")
            } catch (e: URISyntaxException) {
                throw e
            }
        }
        single<DatabaseReference> {
            Firebase.database.reference
        }
        single<UsersRepository> {
            UsersRepositoryImpl(get<DatabaseReference>())
        }
        single<UsersUseCase> {
            UsersUseCase(get<UsersRepository>())
        }
        single<SharedPreferences> {
            androidContext().getSharedPreferences("ChatApp", Context.MODE_PRIVATE)
        }
        viewModel<UsersViewModel> {
            UsersViewModel(get<UsersUseCase>(), get<SharedPreferencesManager>(), get<ChatRepository>())
        }
        single<Gson> {
            Gson()
        }
        single<SharedPreferencesManager> {
            SharedPreferencesManager(get<SharedPreferences>(), get<Gson>())
        }
        single<ChatRepository> {
            ChatRepositoryImpl(get<Socket>(), get<DatabaseReference>())
        }
        viewModel<ChatViewModel> {
            ChatViewModel(get<ChatRepository>())
        }
    }
}