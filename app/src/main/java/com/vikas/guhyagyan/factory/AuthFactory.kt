package com.vikas.guhyagyan.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vikas.guhyagyan.repository.AuthRepository
import com.vikas.guhyagyan.viewmodel.AuthViewModel

class AuthFactory(private val authRepository: AuthRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AuthViewModel::class.java)){
            AuthViewModel(this.authRepository) as T
        }else{
            throw IllegalArgumentException("View Model Not Found")
        }
    }
}