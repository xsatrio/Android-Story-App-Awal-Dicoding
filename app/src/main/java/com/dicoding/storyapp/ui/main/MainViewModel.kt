package com.dicoding.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.storyapp.data.AppRepository
import com.dicoding.storyapp.data.local.pref.UserModel
import com.dicoding.storyapp.data.local.pref.UserPref

class MainViewModel(private val repository: AppRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
//
//    fun logout() {
//        viewModelScope.launch {
//            repository.logout()
//        }
//    }

}