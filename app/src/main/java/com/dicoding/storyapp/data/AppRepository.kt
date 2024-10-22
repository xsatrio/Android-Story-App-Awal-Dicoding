package com.dicoding.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.storyapp.data.pref.UserPref
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.RegisterResponse
import com.dicoding.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first

class AppRepository(
    private val apiService: ApiService,
    private val pref: UserPref
) {
    suspend fun login(email: String, password: String): Results<LoginResponse> {
        return try {
            val response = apiService.login(email, password)
            Results.Success(response)
        } catch (e: Exception) {
            Results.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun saveToken(token: String) {
        pref.saveToken(token)
    }

    suspend fun register(name: String, email: String, password: String): Results<RegisterResponse> {
        return try {
            val response = apiService.register(name, email, password)
            Results.Success(response)
        } catch (e: Exception) {
            Results.Error(e.message ?: "An error occurred")
        }
    }

    fun getAllStories(): LiveData<Results<List<ListStoryItem>>> = liveData {
        emit(Results.Loading)
        try {
            val token = pref.getToken().first()
            val response = apiService.getAllStories(
                token = "Bearer $token",
                page = null,
                size = null,
                location = 0
            )
            emit(Results.Success(response.listStory))
        } catch (e: Exception) {
            emit(Results.Error(e.message.toString()))
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