package com.example.ayushgupta.ktmy19

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions
        setContentView(R.layout.activity_splash_screen)

        if (Build.VERSION.SDK_INT >= 24){
            imageView.setBackgroundResource(R.drawable.libiter)
        }

        val TIMEOUT_INTERVAL = 2500L
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(MainActivity@this,MainActivity::class.java)
            startActivity(intent)
            finish()
        },TIMEOUT_INTERVAL)
    }
}
