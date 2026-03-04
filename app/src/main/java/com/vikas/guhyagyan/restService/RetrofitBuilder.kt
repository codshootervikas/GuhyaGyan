package com.vikas.guhyagyan.restService

import android.app.Application
import android.content.Intent
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import com.vikas.guhyagyan.LoginSignUpActivity
import com.vikas.guhyagyan.utils.LoginManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitBuilder(application: Application) {

    private val retrofit: Retrofit
    private val loginManager = LoginManager(application)

    init {
        val okHttpClient: OkHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(
                    Interceptor { chain: Interceptor.Chain ->
                        val original = chain.request()
                        val requestBuilder: Request.Builder = original.newBuilder()
                            .addHeader("Authorization", "Bearer ${loginManager.getToken()}")
                            .addHeader("Access-Control-Allow-Origin", "*")
                            .addHeader("Access-Control-Allow-Methods", "GET,POST,PUT, OPTIONS")
                            .method(original.method, original.body)
                        val request: Request = requestBuilder.build()
                        val response: Response = chain.proceed(request)
                        if (response.code == 401) {
                            val intent = Intent(
                                application.applicationContext,
                                LoginSignUpActivity::class.java
                            )
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            application.applicationContext.startActivity(intent)
                        }
                        return@Interceptor response
                    }
                )
                .addInterceptor(ChuckerInterceptor(application))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .hostnameVerifier { _, _ -> true }
                .build()
        val gson = GsonBuilder().setLenient().create()
        retrofit = Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    val api: ApiInterface
        get() = retrofit.create(ApiInterface::class.java)

    companion object {
        const val BASEURL = "https://9vlh6582-3000.inc1.devtunnels.ms/api/"
        private var mInstance: RetrofitBuilder? = null

        @Synchronized
        fun getInstance(application: Application): RetrofitBuilder? {
            if (mInstance == null) {
                mInstance = RetrofitBuilder(application)
            }
            return mInstance
        }

    }

}

