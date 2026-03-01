package com.vikas.guhyagyan

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.vikas.guhyagyan.activity.FirstMainActivity
import com.vikas.guhyagyan.activity.LoginActivity
import com.vikas.guhyagyan.databinding.ActivityMainBinding
import com.vikas.guhyagyan.utils.LoginManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val loginManager by lazy { LoginManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            if (loginManager.getToken().isNullOrEmpty())
                startActivity(Intent(this, LoginActivity::class.java))
            else
                startActivity(Intent(this, FirstMainActivity::class.java))
            finish()
        }, 2000)


    }
}
