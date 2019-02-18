package com.example.chen.coolweatherkotlin

import android.app.Application

/**
 * Coder : chenshuaiyu
 * Time : 2019/2/9 18:19
 */
class App : Application() {
    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}