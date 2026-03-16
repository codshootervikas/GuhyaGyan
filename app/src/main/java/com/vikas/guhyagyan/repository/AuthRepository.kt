package com.vikas.guhyagyan.repository

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vikas.guhyagyan.apiState.ApiState
import com.vikas.guhyagyan.models.CommonResponse
import com.vikas.guhyagyan.models.ErrorResponse
import com.vikas.guhyagyan.models.file.FileRemoveRequest
import com.vikas.guhyagyan.models.file.FileUploadResponse
import com.vikas.guhyagyan.models.login.LoginRequest
import com.vikas.guhyagyan.models.login.LoginResponse
import com.vikas.guhyagyan.models.pdf.PDFResponse
import com.vikas.guhyagyan.models.register.RegisterRequest
import com.vikas.guhyagyan.models.register.RegistrationResponse
import com.vikas.guhyagyan.models.verify_otp.VerifyOtpRequest
import com.vikas.guhyagyan.models.verify_otp.VerifyOtpResponse
import com.vikas.guhyagyan.restService.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class AuthRepository(private val apiInterface: ApiInterface) {

    private val registerLiveData = MutableLiveData<ApiState<RegistrationResponse>>()
    val register get() = registerLiveData
    suspend fun registerApi(request: RegisterRequest) {
        registerLiveData.postValue(ApiState.Loading())
        try {
            val response = withContext(Dispatchers.IO) { apiInterface.registrationApi(request) }
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


    private val verifyOtpLiveData = MutableLiveData<ApiState<VerifyOtpResponse>>()
    val verifyOtp get() = verifyOtpLiveData
    suspend fun verifyOtpApi(request: VerifyOtpRequest) {
        verifyOtpLiveData.postValue(ApiState.Loading())
        try {
            val response = withContext(Dispatchers.IO) { apiInterface.verifyOtpApi(request) }
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


    private val loginLiveData = MutableLiveData<ApiState<LoginResponse>>()
    val login get() = loginLiveData
    suspend fun loginApi(request: LoginRequest) {
        loginLiveData.postValue(ApiState.Loading())
        try {
            val response = withContext(Dispatchers.IO) { apiInterface.loginApi(request) }
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


    private val fileSaveLiveData = MutableLiveData<ApiState<FileUploadResponse>>()
    val fileSave get() = fileSaveLiveData
    suspend fun fileSaveApi(image: MultipartBody.Part) {
        fileSaveLiveData.postValue(ApiState.Loading())
        try {
            val response = withContext(Dispatchers.IO) { apiInterface.fileUpload(image) }
            if (response.isSuccessful && response.body() != null) {
                fileSaveLiveData.postValue(ApiState.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                if (errorBody != null) {
                    val errorResponse: ErrorResponse = Gson().fromJson(
                        errorBody, object : TypeToken<ErrorResponse>() {}.type
                    )
                    fileSaveLiveData.postValue(ApiState.Error(errorResponse.message))
                } else {
                    fileSaveLiveData.postValue(
                        ApiState.Error(
                            response.message() ?: "Something went wrong"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            fileSaveLiveData.postValue(ApiState.Error(e.message ?: "Unknown error"))
        }
    }


    private val fileRemoveLiveData = MutableLiveData<ApiState<CommonResponse>>()
    val fileRemove get() = fileRemoveLiveData
    suspend fun fileRemoveApi(request: FileRemoveRequest) {
        fileRemoveLiveData.postValue(ApiState.Loading())
        try {
            val response = apiInterface.fileRemove(request)
            if (response.isSuccessful && response.body() != null) {
                fileRemoveLiveData.postValue(ApiState.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                if (errorBody != null) {
                    val errorResponse: ErrorResponse = Gson().fromJson(
                        errorBody, object : TypeToken<ErrorResponse>() {}.type
                    )
                    fileRemoveLiveData.postValue(ApiState.Error(errorResponse.message))
                } else {
                    fileRemoveLiveData.postValue(
                        ApiState.Error(
                            response.message() ?: "Something went wrong"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            fileRemoveLiveData.postValue(ApiState.Error(e.message ?: "Unknown error"))
        }
    }

    private val getPdfLiveData = MutableLiveData<ApiState<PDFResponse>>()
    val getPdf get() = getPdfLiveData

    suspend fun pdfApi() {
        getPdfLiveData.postValue(ApiState.Loading())
        try {
            val response = apiInterface.getPdfFile()
            if (response.isSuccessful && response.body() != null) {
                getPdfLiveData.postValue(ApiState.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                if (errorBody != null) {
                    val errorResponse: ErrorResponse = Gson().fromJson(
                        errorBody, object : TypeToken<ErrorResponse>() {}.type
                    )
                    getPdfLiveData.postValue(ApiState.Error(errorResponse.message))
                } else {
                    getPdfLiveData.postValue(
                        ApiState.Error(
                            response.message() ?: "Something went wrong"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            getPdfLiveData.postValue(ApiState.Error(e.message ?: "Unknown error"))
        }
        }


}