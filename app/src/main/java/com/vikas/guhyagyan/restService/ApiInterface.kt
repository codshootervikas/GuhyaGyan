package com.vikas.guhyagyan.restService

import com.vikas.guhyagyan.models.register.RegisterRequest
import com.vikas.guhyagyan.models.register.RegistrationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("api/auth/register")
    suspend fun registrationApi(@Body registerRequest: RegisterRequest): Response<RegistrationResponse>

}