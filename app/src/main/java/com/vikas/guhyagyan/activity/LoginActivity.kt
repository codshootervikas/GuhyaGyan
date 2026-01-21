package com.vikas.guhyagyan.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vikas.guhyagyan.R
import com.vikas.guhyagyan.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(R.layout.activity_login) {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttonClick()
    }
    private fun buttonClick() {

        binding.registerBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.loginBtn.setOnClickListener {
            if (validation()) {
                Toast.makeText(this, "Valid Data!", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun validation(): Boolean {

        val phone = binding.etPhone.text.toString().trim()
        var isValid = true

        if (phone.isEmpty()) {
            binding.phoneLayout.error = "Phone is required"
            isValid = false
        } else if (!phone.matches(Regex("^[6-9][0-9]{9}$"))) {
            binding.phoneLayout.error = "Enter valid 10-digit phone"
            isValid = false
        } else {
            binding.phoneLayout.error = null
        }

        return isValid

    }

}