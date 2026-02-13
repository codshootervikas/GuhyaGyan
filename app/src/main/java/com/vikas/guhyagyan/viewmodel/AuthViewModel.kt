package com.vikas.guhyagyan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vikas.guhyagyan.models.register.RegisterRequest
import com.vikas.guhyagyan.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val register get() = authRepository.register
    fun registerApi(request: RegisterRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.registerApi(request)
        }
    }

}