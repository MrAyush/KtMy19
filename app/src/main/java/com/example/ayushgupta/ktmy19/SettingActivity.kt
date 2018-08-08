package com.example.ayushgupta.ktmy19

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setSupportActionBar(toolbar1)
        val spf = getSharedPreferences("Write_to_cal", Context.MODE_PRIVATE)
        write_to_cal.isChecked = spf.getBoolean("calWrite", true)
        Log.w("Setting Class", "Inside Setting class onCreate")
        write_to_cal.setOnCheckedChangeListener { _, isChecked ->
            val spe = spf.edit()
            spe.putBoolean("calWrite", isChecked)
            spe.apply()
        }
    }
}
