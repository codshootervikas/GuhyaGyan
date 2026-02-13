package com.vikas.guhyagyan.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vikas.guhyagyan.R
import com.vikas.guhyagyan.databinding.ActivityValidateOtpBinding

class ValidateOtpActivity : AppCompatActivity(R.layout.activity_validate_otp) {
    private lateinit var binding: ActivityValidateOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityValidateOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        val key = intent.getStringExtra("key")


    }

}