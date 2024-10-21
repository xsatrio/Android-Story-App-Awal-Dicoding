package com.dicoding.storyapp.di
import android.content.Context
import com.dicoding.storyapp.data.AppRepository
import com.dicoding.storyapp.data.local.pref.UserPref
import com.dicoding.storyapp.data.local.pref.dataStore
import com.dicoding.storyapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideAppRepository(context: Context): AppRepository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPref.getInstance(context.dataStore)
        return AppRepository.getInstance(apiService, pref)
    }
}