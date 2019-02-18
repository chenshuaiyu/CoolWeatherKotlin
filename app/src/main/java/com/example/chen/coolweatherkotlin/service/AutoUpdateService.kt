package com.example.chen.coolweatherkotlin.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.preference.PreferenceManager
import android.util.Log
import com.example.chen.coolweatherkotlin.util.HttpUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class AutoUpdateService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        updateWeather()
        updateBingPic()

        val manager = getSystemService(ALARM_SERVICE) as AlarmManager
        val anHour: Int = 8 * 60 * 60 * 1000 //8小时的毫秒数
        val triggerAtTime = SystemClock.elapsedRealtime() + anHour
        val i = Intent(this@AutoUpdateService, AutoUpdateService::class.java)
        val pi = PendingIntent.getService(this@AutoUpdateService, 0, i, 0)
        manager.cancel(pi)
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi)
        return super.onStartCommand(intent, flags, startId)
    }

    //更新背景
    private fun updateBingPic() {
        val requestBingPic = "http://guolin.tech/api/bing_pic"
        HttpUtil.sendOkHttpRequest(requestBingPic, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val bingPic = response.body()?.string()
                val edit = PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService).edit()
                edit.putString("bing_pic", bingPic)
                edit.apply()
            }
        })
    }

    //更新天气
    private fun updateWeather() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService)
        val weatherString = pref.getString("weather", null)
        if (weatherString != null) {
            val weather = HttpUtil.handleWeatherResponse(weatherString)
            val weatherId = weather!!.basic!!.id
            val weatherUrl =
                "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=9086a4fed3d146c7890cad63669d9380"

            HttpUtil.sendOkHttpRequest(weatherUrl, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val r = response.body()?.string()
                    val w = HttpUtil.handleWeatherResponse(r!!)
                    if (w != null && w.status.equals("ok")) {
                        val edit = PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService).edit()
                        edit.putString("weather", r)
                        edit.apply()
                    }
                }
            })
        }
    }
}
