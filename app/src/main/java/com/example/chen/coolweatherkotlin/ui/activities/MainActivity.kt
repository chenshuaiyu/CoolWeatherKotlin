package com.example.chen.coolweatherkotlin.ui.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import com.example.chen.coolweatherkotlin.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
        if (pref.getString("weather", null) != null){
            startActivity(Intent(this@MainActivity, WeatherActivity::class.java))
            finish()
        }
    }
}
