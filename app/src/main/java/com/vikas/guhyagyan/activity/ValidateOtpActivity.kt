package com.vikas.guhyagyan.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.vikas.guhyagyan.MainActivity
import com.vikas.guhyagyan.R
import com.vikas.guhyagyan.apiState.ApiState
import com.vikas.guhyagyan.databinding.ActivityValidateOtpBinding
import com.vikas.guhyagyan.factory.AuthFactory
import com.vikas.guhyagyan.models.verify_otp.VerifyOtpRequest
import com.vikas.guhyagyan.repository.AuthRepository
import com.vikas.guhyagyan.restService.RetrofitBuilder
import com.vikas.guhyagyan.utils.LoginManager
import com.vikas.guhyagyan.viewmodel.AuthViewModel

class ValidateOtpActivity : AppCompatActivity(R.layout.activity_validate_otp) {
    private lateinit var binding: ActivityValidateOtpBinding

    private val authRepository by lazy {
        AuthRepository(RetrofitBuilder.getInstance(application)!!.api)
    }

    private val authViewModel by lazy {
        ViewModelProvider(this, AuthFactory(authRepository))[AuthViewModel::class.java]
    }

    private val loginManager by lazy { LoginManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityValidateOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        val key = intent.getStringExtra("key")
        val email = intent.getStringExtra("email")

        binding.verifyBtn.setOnClickListener {
            if (binding.firstPinView.length() != 4)
                Snackbar.make(binding.root, "Enter valid OTP", Snackbar.LENGTH_SHORT).show()
            else {
                authViewModel.verifyOtpApi(
                    VerifyOtpRequest(
                        email!!,
                        binding.firstPinView.text.toString(),
                        key!!
                    )
                )
            }
        }

        authViewModel.verifyOtp.observe(this) {
            when (it) {
                is ApiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ApiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (it.data?.success == true && !it.data.data?.token.isNullOrEmpty()) {
                        loginManager.setToken(it.data.data.token)
                        startActivity(Intent(this, FirstMainActivity::class.java))
                        finish()
                    }
                    Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT).show()
                }

                is ApiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, it.errorMessage, Snackbar.LENGTH_SHORT).show()
                }
            }
        }


    }


}

