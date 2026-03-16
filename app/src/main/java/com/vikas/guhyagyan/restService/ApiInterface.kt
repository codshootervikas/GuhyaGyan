package com.vikas.guhyagyan.restService

import com.vikas.guhyagyan.models.CommonResponse
import com.vikas.guhyagyan.models.file.FileRemoveRequest
import com.vikas.guhyagyan.models.file.FileUploadResponse
import com.vikas.guhyagyan.models.login.LoginRequest
import com.vikas.guhyagyan.models.login.LoginResponse
import com.vikas.guhyagyan.models.pdf.PDFResponse
import com.vikas.guhyagyan.models.register.RegisterRequest
import com.vikas.guhyagyan.models.register.RegistrationResponse
import com.vikas.guhyagyan.models.verify_otp.VerifyOtpRequest
import com.vikas.guhyagyan.models.verify_otp.VerifyOtpResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface {

    @POST("auth/register")
    suspend fun registrationApi(@Body registerRequest: RegisterRequest): Response<RegistrationResponse>

    @POST("auth/verify-otp")
    suspend fun verifyOtpApi(@Body verifyOtpRequest: VerifyOtpRequest): Response<VerifyOtpResponse>

    @POST("auth/login")
    suspend fun loginApi(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @Multipart
    @POST("file/image/upload")
    suspend fun fileUpload(@Part file: MultipartBody.Part): Response<FileUploadResponse>

    @POST("file/image/remove")
    suspend fun fileRemove(@Body fileRemoveRequest: FileRemoveRequest): Response<CommonResponse>

    @GET("pdf/my")
    suspend fun getPdfFile(): Response<PDFResponse>

    @Multipart
    @POST("upload-audio")
    suspend fun uploadAudio(
        @Part audio: MultipartBody.Part
    ): Response<CommonResponse>

}