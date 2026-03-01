package com.vikas.guhyagyan.restservice

import com.vikas.guhyagyan.models.CommonResponse
import com.vikas.guhyagyan.models.login.LoginRequest
import com.vikas.guhyagyan.models.register.RegisterRequest
import com.vikas.guhyagyan.models.register.RegistrationResponse
import com.vikas.guhyagyan.models.verify_otp.VerifyOtpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("auth/register")
    suspend fun registrationApi(@Body registerRequest: RegisterRequest): Response<RegistrationResponse>

    @POST("auth/verify-otp")
    suspend fun verifyOtpApi(@Body verifyOtpRequest: VerifyOtpRequest): Response<CommonResponse>

    @POST("auth/login")
    suspend fun loginApi(@Body loginRequest: LoginRequest): Response<CommonResponse>

}