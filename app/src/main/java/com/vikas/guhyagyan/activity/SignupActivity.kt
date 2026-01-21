package com.vikas.guhyagyan.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vikas.guhyagyan.R
import com.vikas.guhyagyan.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity(R.layout.activity_signup) {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    private fun init() {

        binding.registerBtn.setOnClickListener {
            if (validateFields()) {
                Toast.makeText(this, "Valid Data!", Toast.LENGTH_SHORT).show()
            }
        }


    }
    private fun validateFields(): Boolean {
        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()

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
        }else if (email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.error = "Enter valid email"
            isValid = false
        } else {
            binding.emailLayout.error = null
        }

        // Address Validation (Optional)
        if (address.isEmpty()) {
            binding.addressLayout.error = "Address is required"
            isValid = false
        } else {
            binding.addressLayout.error = null
        }

        return isValid
    }

}