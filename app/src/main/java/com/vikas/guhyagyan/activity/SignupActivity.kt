package com.vikas.guhyagyan.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vikas.guhyagyan.R
import com.vikas.guhyagyan.apiState.ApiState
import com.vikas.guhyagyan.databinding.ActivitySignupBinding
import com.vikas.guhyagyan.factory.AuthFactory
import com.vikas.guhyagyan.models.register.RegisterRequest
import com.vikas.guhyagyan.repository.AuthRepository
import com.vikas.guhyagyan.restService.RetrofitBuilder
import com.vikas.guhyagyan.viewmodel.AuthViewModel

class SignupActivity : AppCompatActivity(R.layout.activity_signup) {
    private lateinit var binding: ActivitySignupBinding

    private val authRepository by lazy {
        AuthRepository(RetrofitBuilder.getInstance(application)!!.api)
    }

    private val authViewModel by lazy {
        ViewModelProvider(this, AuthFactory(authRepository))[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        authViewModel.register.observe(this) {
            when (it) {
                is ApiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ApiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT).show()
                    it.data?.data?.verifyKey?.let { key ->
                        startActivity(Intent(this, ValidateOtpActivity::class.java)
                            .putExtra("key", key)
                            .putExtra("email", binding.etEmail.text.toString().trim()))
                    }
                }

                is ApiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }

            }
        }

        binding.registerBtn.setOnClickListener {
            if (validateFields()) {
                authViewModel.registerApi(
                    RegisterRequest(
                        email = binding.etEmail.text.toString().trim(),
                        phone = binding.etPhone.text.toString().trim(),
                        name = binding.etName.text.toString().trim(),
                        password = binding.etPassword1.text.toString().trim(),
                    )
                )
            }
        }

    }

    private fun validateFields(): Boolean {
        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val pass1 = binding.etPassword1.text.toString().trim()
        val pass2 = binding.etPassword2.text.toString().trim()

        var isValid = true

        // Name Validation
        if (name.isEmpty()) {
            binding.nameLayout.error = "Name is required"
            isValid = false
        } else {
            binding.nameLayout.error = null
        }

        // Phone Validation (10 digits)
        if (phone.isEmpty()) {
            binding.phoneLayout.error = "Phone is required"
            isValid = false
        } else if (!phone.matches(Regex("^[6-9][0-9]{9}$"))) {
            binding.phoneLayout.error = "Enter valid 10-digit phone"
            isValid = false
        } else {
            binding.phoneLayout.error = null
        }

        // Email Validation (Optional but must be valid if entered)
        if (email.isEmpty()) {
            binding.emailLayout.error = "Email is required"
            isValid = false
        } else if (email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) {
            binding.emailLayout.error = "Enter valid email"
            isValid = false
        } else {
            binding.emailLayout.error = null
        }


        if (pass1.isEmpty()) {
            binding.passwordLayout1.error = "password is required"
            isValid = false
        } else {
            binding.passwordLayout1.error = null
        }

        if (pass2.isEmpty()) {
            binding.passwordLayout2.error = "confirm password is required"
            isValid = false
        } else {
            binding.passwordLayout2.error = null
        }

        if (pass1 != pass2) {
            binding.passwordLayout2.error = "Password does not match"
            isValid = false
        } else {
            binding.passwordLayout2.error = null
        }

        return isValid
    }

}