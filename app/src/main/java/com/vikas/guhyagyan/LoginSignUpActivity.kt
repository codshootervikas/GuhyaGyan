package com.vikas.guhyagyan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vikas.guhyagyan.activity.LoginActivity
import com.vikas.guhyagyan.activity.SignupActivity
import com.vikas.guhyagyan.databinding.ActivityLoginSignUpBinding

class LoginSignUpActivity : AppCompatActivity(R.layout.activity_login_sign_up) {
    private lateinit var binding: ActivityLoginSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signupBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

    }

}