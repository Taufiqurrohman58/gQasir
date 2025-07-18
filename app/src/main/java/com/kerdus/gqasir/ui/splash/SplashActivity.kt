package com.kerdus.gqasir.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kerdus.gqasir.R
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.kerdus.gqasir.Main2Activity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)

        // Menampilkan splash screen selama 3 detik
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Main2Activity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}