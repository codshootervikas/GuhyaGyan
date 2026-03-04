package com.vikas.guhyagyan.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.vikas.guhyagyan.R
import com.vikas.guhyagyan.apiState.ApiState
import com.vikas.guhyagyan.databinding.ActivityLoginBinding
import com.vikas.guhyagyan.factory.AuthFactory
import com.vikas.guhyagyan.models.login.LoginRequest
import com.vikas.guhyagyan.repository.AuthRepository
import com.vikas.guhyagyan.restService.RetrofitBuilder
import com.vikas.guhyagyan.utils.LoginManager
import com.vikas.guhyagyan.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity(R.layout.activity_login) {
    private lateinit var binding: ActivityLoginBinding
    private val loginManager by lazy { LoginManager(this) }

    private val authRepository by lazy {
        AuthRepository(RetrofitBuilder.getInstance(application)!!.api)
    }

    private val authViewModel by lazy {
        ViewModelProvider(this, AuthFactory(authRepository))[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        binding.registerBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.loginBtn.setOnClickListener {
            if (validation()) {
                if (binding.etPhone.text.toString().isDigitsOnly())
                    authViewModel.loginApi(
                        LoginRequest(
                            email = null,
                            phone = binding.etPhone.text.toString().trim(),
                            password = binding.etPassword.text.toString().trim()
                        )
                    )
                else
                    authViewModel.loginApi(
                        LoginRequest(
                            email = binding.etPhone.text.toString().trim(),
                            phone = null,
                            password = binding.etPassword.text.toString().trim()
                        )
                    )
            }
        }

        authViewModel.login.observe(this) {
            when (it) {
                is ApiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ApiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (it.data?.success == true && !it.data.data?.token.isNullOrEmpty()) {
                        loginManager.setToken(it.data.data.token)
                        startActivity(Intent(this, FirstMainActivity::class.java))
                        finishAffinity()
                    } else
                        Snackbar.make(
                            binding.root,
                            it.data?.message.toString(),
                            Snackbar.LENGTH_SHORT
                        ).show()
                }

                is ApiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, it.errorMessage, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun validation(): Boolean {

        val input = binding.etPhone.text.toString().trim()
        var isValid = true

        if (input.isEmpty()) {
            binding.phoneLayout.error = "Phone or Email is required"
            return false
        }

        if (input.isDigitsOnly()) {
            // 📱 Phone number validation (India)
            if (!input.matches(Regex("^[6-9][0-9]{9}$"))) {
                binding.phoneLayout.error = "Enter valid 10-digit phone number"
                isValid = false
            } else {
                binding.phoneLayout.error = null
            }
        } else {
            // 📧 Email validation
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                binding.phoneLayout.error = "Enter valid email address"
                isValid = false
            } else {
                binding.phoneLayout.error = null
            }
        }

        return isValid
    }

}