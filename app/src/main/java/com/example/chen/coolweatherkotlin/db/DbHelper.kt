package com.example.chen.coolweatherkotlin.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.chen.coolweatherkotlin.App

/**
 * Coder : chenshuaiyu
 * Time : 2019/2/9 18:13
 */
class DbHelper(context: Context? = App.instance) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "cool_weather.db"
        const val DB_VERSION = 1
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("CREATE TABLE Province(" +
                "id integer primary key autoincrement," +
                "provinceName text," +
                "provinceCode integer" +
                ");")
        p0?.execSQL("CREATE TABLE City(" +
                "id integer primary key autoincrement," +
                "cityName text," +
                "cityCode integer," +
                "provinceId integer" +
                ");")
        p0?.execSQL("CREATE TABLE County(" +
                "id integer primary key autoincrement," +
                "countyName text," +
                "weatherId text," +
                "cityId integer" +
                ");")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

}