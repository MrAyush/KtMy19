package com.example.ayushgupta.ktmy19

import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setSupportActionBar(toolbar1)
        var isAllowed = true
        val manager = packageManager
        val spf = getSharedPreferences("Write_to_cal", Context.MODE_PRIVATE)
        if (manager.checkPermission(android.Manifest.permission.WRITE_CALENDAR, "com.example.ayushgupta.ktmy19") == PackageManager.PERMISSION_DENIED) {
            isAllowed = false
        }
        write_to_cal.isChecked = spf.getBoolean("calWrite", isAllowed)
        Log.w("Setting Class", "Inside Setting class onCreate")
        Log.w("isChecked", "Inside Setting class ${write_to_cal.isChecked}")
        write_to_cal.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkMyPermission(android.Manifest.permission.WRITE_CALENDAR)
            }
            val spe = spf.edit()
            spe.putBoolean("calWrite", isChecked)
            spe.apply()
        }
    }

    private fun checkMyPermission(str: String): Boolean {
        val status = ContextCompat.checkSelfPermission(this, str)
        return if (status == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(str), 100)
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "Need permission for it!!", Toast.LENGTH_SHORT).show()
            write_to_cal.isChecked = false
        } else {
            recreate()
        }
    }
}
