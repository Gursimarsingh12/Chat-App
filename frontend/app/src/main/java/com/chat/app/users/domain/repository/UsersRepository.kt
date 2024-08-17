package com.chat.app.users.domain.repository

import com.chat.app.core.utils.Resource
import com.chat.app.users.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    suspend fun login(user: User): Flow<Resource<User>>
    suspend fun signUp(user: User): Flow<Resource<User>>
    suspend fun getAllUsers(currentEmail: String): Flow<Resource<List<User>>>
}