package com.vikas.guhyagyan.repository

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vikas.guhyagyan.apiState.ApiState
import com.vikas.guhyagyan.models.CommonResponse
import com.vikas.guhyagyan.models.ErrorResponse
import com.vikas.guhyagyan.models.login.LoginRequest
import com.vikas.guhyagyan.models.register.RegisterRequest
import com.vikas.guhyagyan.models.register.RegistrationResponse
import com.vikas.guhyagyan.models.verify_otp.VerifyOtpRequest
import com.vikas.guhyagyan.restservice.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val apiInterface: ApiInterface) {

    private val registerLiveData = MutableLiveData<ApiState<RegistrationResponse>>()
    val register get() = registerLiveData
    suspend fun registerApi(request: RegisterRequest) {
        registerLiveData.postValue(ApiState.Loading())
        try {
            val response = withContext(Dispatchers.IO) { apiInterface.registrationApi(request)}
            if (response.isSuccessful && response.body() != null) {
                registerLiveData.postValue(ApiState.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                if (errorBody != null) {
                    val errorResponse: ErrorResponse = Gson().fromJson(
                        errorBody, object : TypeToken<ErrorResponse>() {}.type
                    )
                    registerLiveData.postValue(ApiState.Error(errorResponse.message))
                } else {
                    registerLiveData.postValue(
                        ApiState.Error(
                            response.message() ?: "Something went wrong"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            registerLiveData.postValue(ApiState.Error(e.message ?: "Unknown error"))
        }
    }


    private val verifyOtpLiveData = MutableLiveData<ApiState<CommonResponse>>()
    val verifyOtp get() = verifyOtpLiveData

    suspend fun verifyOtpApi(request: VerifyOtpRequest) {
        verifyOtpLiveData.postValue(ApiState.Loading())
        try {
            val response = withContext(Dispatchers.IO) { apiInterface.verifyOtpApi(request)}
            if (response.isSuccessful && response.body() != null) {
                verifyOtpLiveData.postValue(ApiState.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                if (errorBody != null) {
                    val errorResponse: ErrorResponse = Gson().fromJson(
                        errorBody, object : TypeToken<ErrorResponse>() {}.type
                    )
                    verifyOtpLiveData.postValue(ApiState.Error(errorResponse.message))
                } else {
                    verifyOtpLiveData.postValue(
                        ApiState.Error(
                            response.message() ?: "Something went wrong"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            verifyOtpLiveData.postValue(ApiState.Error(e.message ?: "Unknown error"))
        }
    }
    private val loginLiveData = MutableLiveData<ApiState<CommonResponse>>()
    val login get() = loginLiveData

    suspend fun loginApi(request: LoginRequest) {
        loginLiveData.postValue(ApiState.Loading())
        try {
            val response = withContext(Dispatchers.IO) { apiInterface.loginApi(request)}
            if (response.isSuccessful && response.body() != null) {
                loginLiveData.postValue(ApiState.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                if (errorBody != null) {
                    val errorResponse: ErrorResponse = Gson().fromJson(
                        errorBody, object : TypeToken<ErrorResponse>() {}.type
                    )
                    loginLiveData.postValue(ApiState.Error(errorResponse.message))
                } else {
                    loginLiveData.postValue(
                        ApiState.Error(
                            response.message() ?: "Something went wrong"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            loginLiveData.postValue(ApiState.Error(e.message ?: "Unknown error"))
        }
    }

}