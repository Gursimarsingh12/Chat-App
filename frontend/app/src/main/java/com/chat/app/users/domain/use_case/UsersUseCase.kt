package com.chat.app.users.domain.use_case

import com.chat.app.users.domain.models.User
import com.chat.app.users.domain.repository.UsersRepository

class UsersUseCase(
    private val repository: UsersRepository
) {
    suspend fun login(user: User) = repository.login(user)
    suspend fun signUp(user: User) = repository.signUp(user)
    suspend fun getAllUsers(currentEmail: String) = repository.getAllUsers(currentEmail)
}