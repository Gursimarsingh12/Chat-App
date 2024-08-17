package com.chat.app.users.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chat.app.chats.domain.models.Message
import com.chat.app.chats.domain.repository.ChatRepository
import com.chat.app.core.utils.Resource
import com.chat.app.shared_preferences.SharedPreferencesManager
import com.chat.app.users.domain.models.SignUpScreenState
import com.chat.app.users.domain.models.User
import com.chat.app.users.domain.use_case.UsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

class UsersViewModel(
    private val usersUseCase: UsersUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val _userState = MutableStateFlow(SignUpScreenState<User>())
    val userState: StateFlow<SignUpScreenState<User>> = _userState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(sharedPreferencesManager.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _users = MutableStateFlow(SignUpScreenState<List<User>>())
    val users: StateFlow<SignUpScreenState<List<User>>> = _users.asStateFlow()

    private val _message = MutableStateFlow(SignUpScreenState<Message>())
    val message: StateFlow<SignUpScreenState<Message>> = _message.asStateFlow()

    fun onNameChanged(name: String) {
        _userState.update {
            it.copy(
                name = name,
                nameError = if (name.isEmpty()) "Name is required" else ""
            )
        }
    }

    fun onEmailChanged(email: String) {
        _userState.update {
            it.copy(
                email = email,
                emailError = when {
                    email.isEmpty() -> "Email is required"
                    Patterns.EMAIL_ADDRESS.matcher(email).matches().not() -> "Invalid email"
                    else -> ""
                }
            )
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun signUp() {
        val user = User(
            name = _userState.value.name,
            email = _userState.value.email,
            id = Uuid.random().toString()
        )
        if (_userState.value.emailError.isEmpty() && _userState.value.nameError.isEmpty()) {
            viewModelScope.launch {
                usersUseCase.signUp(user).collect { state ->
                    _userState.update {
                        it.copy(
                            state = state
                        )
                    }
                    if (state is Resource.Success) {
                        sharedPreferencesManager.setLoggedIn(true)
                        sharedPreferencesManager.setUser(user)
                        _isLoggedIn.value = true
                    }
                }
            }
        }
    }

    fun login() {
        val user = User(
            name = _userState.value.name,
            email = _userState.value.email
        )
        if (_userState.value.emailError.isEmpty() && _userState.value.nameError.isEmpty()) {
            viewModelScope.launch {
                usersUseCase.login(user).collect { state ->
                    _userState.update {
                        it.copy(
                            state = state
                        )
                    }
                    if (state is Resource.Success) {
                        sharedPreferencesManager.setLoggedIn(true)
                        sharedPreferencesManager.setUser(user)
                        _isLoggedIn.value = true
                    }
                }
            }
        }
    }

    fun getStoredUser(): User? {
        return sharedPreferencesManager.getUser()
    }

    fun logOut() {
        sharedPreferencesManager.setLoggedIn(false)
        sharedPreferencesManager.clearUser()
        _isLoggedIn.value = false
    }

    fun getAllUsers(currentEmail: String) {
        viewModelScope.launch {
            usersUseCase.getAllUsers(currentEmail).collect { resource ->
                _users.update {
                    it.copy(
                        state = resource,
                        users = (resource as? Resource.Success)?.data ?: emptyList()
                    )
                }
            }
        }
    }

    fun getLatestMessages(senderId: String, receiverId: String) {
        viewModelScope.launch {
            chatRepository.getLatestMessage(senderId, receiverId).collectLatest { state ->
                if (state is Resource.Success && state.data != null) {
                    _message.update {
                        it.copy(
                            state = state,
                            message = state.data
                        )
                    }
                }
            }
        }
    }
}
