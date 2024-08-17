package com.chat.app.users.data.repository

import android.util.Log
import com.chat.app.core.utils.Resource
import com.chat.app.users.domain.models.User
import com.chat.app.users.domain.repository.UsersRepository
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class UsersRepositoryImpl(
    private val db: DatabaseReference
) : UsersRepository {

    override suspend fun login(user: User): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        try {
            val userReference = db.child("users")
            val snapshot = userReference.get().await()
            val users = snapshot.children.mapNotNull { it.getValue(User::class.java) }
            val foundUser = users.find { it.email == user.email && it.name == user.name }
            if (foundUser != null) {
                emit(Resource.Success(foundUser))
            } else {
                emit(Resource.Error("User not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }

    override suspend fun signUp(user: User): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        try {
            val userReference = db.child("users")
            val snapshot = userReference.get().await()
            val users = snapshot.children.mapNotNull { it.getValue(User::class.java) }
            val existingUser = users.find { it.email == user.email && it.name == user.name }

            if (existingUser != null) {
                emit(Resource.Error("Email already in use"))
            } else {
                db.child("users").child(user.id).setValue(user).await()
                emit(Resource.Success(user))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }

    override suspend fun getAllUsers(currentEmail: String): Flow<Resource<List<User>>> = flow {
        emit(Resource.Loading)
        try {
            val userReference = db.child("users")
            val snapshot = userReference.get().await()
            val users = snapshot.children.mapNotNull { it.getValue(User::class.java) }
            val filteredUsers = users.filter { it.email != currentEmail }
            Log.d("UsersRepositoryImpl", "getAllUsers: $filteredUsers")
            emit(Resource.Success(filteredUsers))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }
}
