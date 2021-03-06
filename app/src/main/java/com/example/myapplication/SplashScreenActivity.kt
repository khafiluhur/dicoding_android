package com.example.myapplication

import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.content.Intent
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }, delay)
    }

    companion object {
        const val delay = 3000L
    }
}