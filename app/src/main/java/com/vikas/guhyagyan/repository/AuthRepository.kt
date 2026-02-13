package com.vikas.guhyagyan.repository

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vikas.guhyagyan.apiState.ApiState
import com.vikas.guhyagyan.models.ErrorResponse
import com.vikas.guhyagyan.models.register.RegisterRequest
import com.vikas.guhyagyan.models.register.RegistrationResponse
import com.vikas.guhyagyan.restService.ApiInterface
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


}