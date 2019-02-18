package com.example.chen.coolweatherkotlin.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.chen.coolweatherkotlin.R
import com.example.chen.coolweatherkotlin.bean.Weather
import com.example.chen.coolweatherkotlin.service.AutoUpdateService
import com.example.chen.coolweatherkotlin.util.HttpUtil
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.aqi.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.now.*
import kotlinx.android.synthetic.main.suggestion.*
import kotlinx.android.synthetic.main.title.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class WeatherActivity : AppCompatActivity() {
    private var mWeatherId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val view = window.decorView
            view.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_weather)

        nav_button.setOnClickListener {
            drawer_layout.openDrawer(Gravity.START)
        }

        swipe_refresh.setColorSchemeResources(R.color.colorPrimary)

        val pref = PreferenceManager.getDefaultSharedPreferences(this@WeatherActivity)
        val weatherString = pref.getString("weather", null)
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            val weather = HttpUtil.handleWeatherResponse(weatherString)
            mWeatherId = weather?.basic?.id
            if (weather != null)
                showWeatherInfo(weather)
        } else {
            //无缓存时去服务器查询天气
            mWeatherId = intent.getStringExtra("weather_id")
            weather_layout.visibility = View.VISIBLE
            requestWeather(mWeatherId)
        }

        swipe_refresh.setOnRefreshListener { requestWeather(mWeatherId) }

        val bingPic = pref.getString("bing_pic", null)
        if (bingPic != null) {
            Glide.with(this@WeatherActivity).load(bingPic).into(bing_pic_img)
        } else {
            loadBingPic()
        }

    }

    /**
     * 加载必应每日一图
     */
    private fun loadBingPic() {
        val requestBingPic = "http://guolin.tech/api/bing_pic"
        HttpUtil.sendOkHttpRequest(requestBingPic, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val bingPic = response.body()!!.string()
                val edit = PreferenceManager.getDefaultSharedPreferences(this@WeatherActivity).edit()
                edit.putString("bing_pic", bingPic)
                edit.apply()
                runOnUiThread {
                    Glide.with(this@WeatherActivity).load(bingPic).into(bing_pic_img)
                }
            }
        })
    }

    //根据天气 id 请求城市天气信息
    fun requestWeather(weatherId: String?) {
        val weatherUrl = "http://guolin.tech/api/weather?cityid=" +
                weatherId + "&key=9086a4fed3d146c7890cad63669d9380"
        HttpUtil.sendOkHttpRequest(weatherUrl, object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val r = response.body()?.string()
                if (r != null) {
                    val weather = HttpUtil.handleWeatherResponse(r)
                    runOnUiThread {
                        if (weather != null && weather.status.equals("ok")) {
                            val edit = PreferenceManager.getDefaultSharedPreferences(this@WeatherActivity).edit()
                            edit.putString("weather", r)
                            edit.apply()

                            mWeatherId = weather.basic?.id
                            showWeatherInfo(weather)
                        } else {
                            Toast.makeText(this@WeatherActivity, "获取天气信息失败", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this@WeatherActivity, "获取天气信息失败", Toast.LENGTH_LONG).show()
                }
                swipe_refresh.setRefreshing(false)
            }

            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(this@WeatherActivity, "获取天气信息失败", Toast.LENGTH_LONG).show()
                swipe_refresh.setRefreshing(false)
            }

        })

    }

    //处理并展示Weather实体类中的数据
    private fun showWeatherInfo(weather: Weather.HeWeatherBean) {
        val cityName = weather.basic?.location
        val updateTime = weather.basic?.update?.loc
        val degree = weather.now?.tmp + "℃"

        title_city.text = cityName
        title_update_time.text = updateTime
        degree_text.text = degree
        forecast_layout.removeAllViews()

        weather.daily_forecast?.forEach {
            val view =
                LayoutInflater.from(this@WeatherActivity).inflate(R.layout.forecast_item, forecast_layout, false)

            val dateTextView = view.findViewById<TextView>(R.id.date_text)
            val maxTextView = view.findViewById<TextView>(R.id.max_text)
            val minTextView = view.findViewById<TextView>(R.id.min_text)

            dateTextView.text = it.date
            maxTextView.text = it.tmp?.max
            minTextView.text = it.tmp?.min
            forecast_layout.addView(view)
        }
        if (weather.aqi != null) {
            aqi_text.text = weather.aqi!!.city!!.aqi
            pm25_text.text = weather.aqi!!.city!!.pm25
        }

        val comfort = "舒适度" + weather.suggestion!!.comf!!.txt
        val carWash = "洗车指数" + weather.suggestion!!.cw!!.txt
        val sport = "运动建议" + weather.suggestion!!.sport!!.txt

        comfort_text.text = comfort
        car_wash_text.text = carWash
        sport_text.text = sport

        weather_layout.visibility = View.VISIBLE

        //开启自动更新服务
        val intent = Intent(this@WeatherActivity, AutoUpdateService::class.java)
        startService(intent)
    }
}
