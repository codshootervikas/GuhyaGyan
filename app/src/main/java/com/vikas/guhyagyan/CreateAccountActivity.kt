package com.vikas.guhyagyan

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vikas.guhyagyan.databinding.ActivityCreateAccountBinding

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnCreate.setOnClickListener {
            validateAndSubmit()
        }
    }

    private fun validateAndSubmit() {

        // Get Values
        val name = binding.etName.text.toString().trim()
        val mobile = binding.etMobile.text.toString().trim()
        val countryCode = binding.ccp.selectedCountryCodeWithPlus
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirm.text.toString().trim()

        // Clear old errors
        binding.layoutName.error = null
        binding.layoutMobile.error = null
        binding.layoutPass.error = null
        binding.layoutConfirm.error = null

        // Validations
        if (name.isEmpty()) {
            binding.layoutName.error = "Name required"
            return
        }

        if (mobile.isEmpty()) {

            binding.layoutMobile.error = "Mobile required"
            return
        }

        if (mobile.length < 10 && !mobile.matches(Regex("^[0-9]{10}$"))) {
            binding.layoutMobile.error = "Enter valid mobile number"
            return
        }

        if (password.isEmpty()) {
            binding.layoutPass.error = "Password required"
            return
        }

        if (password.length < 6) {
            binding.layoutPass.error = "Password must be at least 6 characters"
            return
        }

        if (confirmPassword.isEmpty()) {
            binding.layoutConfirm.error = "Confirm Password required"
            return
        }

        if (password != confirmPassword) {
            binding.layoutConfirm.error = "Password does not match"
            return
        }
        // Final Phone Number
        val fullPhone = "$countryCode$mobile"

        Toast.makeText(this, "Success!\nName: $name\nPhone: $fullPhone", Toast.LENGTH_LONG).show()

        // TODO: Send data to server / Firebase
    }
}