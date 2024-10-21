package com.dicoding.storyapp.data

import com.dicoding.storyapp.data.local.pref.UserModel
import com.dicoding.storyapp.data.local.pref.UserPref
import com.dicoding.storyapp.data.remote.response.RegisterResponse
import com.dicoding.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val apiService: ApiService,
    private val pref: UserPref
) {
    fun getSession(): Flow<UserModel> {
        return pref.getSession()
    }

    suspend fun saveSession(user: UserModel) {
        pref.saveSession(user)
    }

    suspend fun logout() {
        pref.logout()
    }

    suspend fun register(name: String, email: String, password: String): Results<RegisterResponse> {
        return try {
            val response = apiService.register(name, email, password)
            Results.Success(response)
        } catch (e: Exception) {
            Results.Error(e.message ?: "An error occurred")
        }
    }

    companion object {
        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            apiService: ApiService,
            pref: UserPref
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(apiService, pref)
            }.also { instance = it }
    }
}