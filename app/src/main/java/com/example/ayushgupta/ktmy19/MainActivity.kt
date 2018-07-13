package com.example.ayushgupta.ktmy19

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val frag = fragmentManager.beginTransaction()
        frag.add(R.id.home, LoginFrag())
        frag.commit()
    }
}
