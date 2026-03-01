package com.vikas.guhyagyan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vikas.guhyagyan.models.login.LoginRequest
import com.vikas.guhyagyan.models.register.RegisterRequest
import com.vikas.guhyagyan.models.verify_otp.VerifyOtpRequest
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

    val verifyOtp get() = authRepository.verifyOtp
    fun verifyOtpApi(request: VerifyOtpRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.verifyOtpApi(request)
        }
    }

    val login get() = authRepository.login

    fun loginApi(request: LoginRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.loginApi(request)
        }
    }

}