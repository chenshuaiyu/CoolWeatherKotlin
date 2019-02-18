package com.example.chen.coolweatherkotlin.util

import android.text.TextUtils
import android.util.Log
import com.example.chen.coolweatherkotlin.bean.Weather
import com.example.chen.coolweatherkotlin.db.City
import com.example.chen.coolweatherkotlin.db.County
import com.example.chen.coolweatherkotlin.db.DbManager
import com.example.chen.coolweatherkotlin.db.Province
import com.google.gson.Gson
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

/**
 * Coder : chenshuaiyu
 * Time : 2019/2/9 15:33
 */
class HttpUtil {
    companion object {
        fun sendOkHttpRequest(address: String, callback: Callback) {
            val request = Request.Builder().url(address).build()
            OkHttpClient().newCall(request).enqueue(callback)
        }

        /**
         * 解析和处理服务器返回的省级数据
         */
        fun handleProvinceResponse(response: String): Boolean {
            if (!TextUtils.isEmpty(response)) {
                val allProvince = JSONArray(response)
                for (i in 0 until allProvince.length()) {
                    val provinceObject: JSONObject = allProvince.getJSONObject(i)
                    DbManager.addProvince(
                        Province(
                            provinceObject.getString("name"),
                            provinceObject.getInt("id")
                        )
                    )
                }
                return true
            }
            return false
        }

        /**
         * 解析和处理服务器返回的市级数据
         */
        fun handleCityResponse(response: String, provinceId: Int): Boolean {
            if (!TextUtils.isEmpty(response)) {
                val allCities = JSONArray(response)
                for (i in 0 until allCities.length()) {
                    val cityObject = allCities.getJSONObject(i)
                    DbManager.addCity(
                        City(
                            cityObject.getString("name"),
                            cityObject.getInt("id"),
                            provinceId
                        )
                    )
                }
                return true
            }
            return false
        }

        /**
         * 解析和处理服务器返回的县级数据
         */
        fun handleCountyResponse(response: String, cityId: Int): Boolean {
            if (!TextUtils.isEmpty(response)) {
                val allCounties = JSONArray(response)
                for (i in 0 until allCounties.length()) {
                    val countyObject = allCounties.getJSONObject(i)
                    DbManager.addCounty(
                        County(
                            countyObject.getString("name"),
                            countyObject.getString("weather_id"),
                            cityId
                        )
                    )
                }
                return true
            }
            return false
        }

        /**
         * 将返回的JSON数据解析成Weather实体类
         */
        fun handleWeatherResponse(response: String): Weather.HeWeatherBean? {
            val jsonObject = JSONObject(response)
            val jsonArray = jsonObject.getJSONArray("HeWeather")
            val weatherContent = jsonArray.getJSONObject(0).toString()
            return Gson().fromJson(weatherContent, Weather.HeWeatherBean::class.java)
        }
    }
}