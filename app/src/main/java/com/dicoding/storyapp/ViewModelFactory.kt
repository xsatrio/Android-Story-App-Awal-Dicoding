package com.dicoding.storyapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.data.AppRepository
import com.dicoding.storyapp.data.local.pref.UserPref
import com.dicoding.storyapp.data.local.pref.dataStore
import com.dicoding.storyapp.di.Injection
import com.dicoding.storyapp.ui.main.MainViewModel

class ViewModelFactory(private val repository: AppRepository, preferences: UserPref) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }


    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                val preferences = UserPref.getInstance(context.dataStore)
                instance ?: ViewModelFactory(Injection.provideAppRepository(context), preferences)
            }.also { instance = it }
    }
}