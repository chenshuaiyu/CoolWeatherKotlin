package com.example.chen.coolweatherkotlin.ui.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.chen.coolweatherkotlin.R
import com.example.chen.coolweatherkotlin.db.City
import com.example.chen.coolweatherkotlin.db.County
import com.example.chen.coolweatherkotlin.db.DbManager
import com.example.chen.coolweatherkotlin.db.Province
import com.example.chen.coolweatherkotlin.ui.activities.MainActivity
import com.example.chen.coolweatherkotlin.ui.activities.WeatherActivity
import com.example.chen.coolweatherkotlin.util.HttpUtil
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.fragment_choose_area.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * Coder : chenshuaiyu
 * Time : 2019/2/8 14:58
 */
class ChooseAreaFragment : Fragment() {
    //级别
    private val LEVEL_PROVINCE: Int = 0
    private val LEVEL_CITY: Int = 1
    private val LEVEL_COUNTY: Int = 2

    //类型
    private val TYPE_PROVINCE: String = "province"
    private val TYPE_CITY: String = "city"
    private val TYPE_COUNTY: String = "county"

    private var dialog: ProgressDialog? = null

    private lateinit var adapter: ArrayAdapter<String>
    private val dataList: ArrayList<String> = ArrayList()

    //省列表
    private lateinit var provinceList: List<Province>
    //市列表
    private lateinit var cityList: List<City>
    //县列表
    private lateinit var countyList: List<County>

    //选中的省份
    private lateinit var selectedProvince: Province
    //选中的城市
    private lateinit var selectedCity: City

    //当前选中的级别
    private var currentLevel: Int = LEVEL_PROVINCE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_choose_area, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, dataList)
        list_view.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        list_view.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (currentLevel) {
                    LEVEL_PROVINCE -> {
                        selectedProvince = provinceList.get(p2)
                        queryCities()
                    }
                    LEVEL_CITY -> {
                        selectedCity = cityList.get(p2)
                        queryCounties()
                    }
                    LEVEL_COUNTY -> {
                        val weatherId = countyList.get(p2).weatherId
                        if (activity is MainActivity) {
                            val intent = Intent(context, WeatherActivity::class.java)
                            intent.putExtra("weather_id", weatherId)
                            startActivity(intent)
                            (activity as MainActivity).finish()
                        } else if (activity is WeatherActivity) {
                            val activity = activity as WeatherActivity
                            activity.drawer_layout.closeDrawers()
                            activity.swipe_refresh.isRefreshing = true
                            activity.requestWeather(weatherId)
                        }
                    }
                }
            }
        }
        back_button.setOnClickListener {
            when (currentLevel) {
                LEVEL_COUNTY -> queryCities()
                LEVEL_CITY -> queryProvinces()
            }
        }
        queryProvinces()
    }

    override fun onDestroyView() {
        closeProgressDialog()
        super.onDestroyView()
    }

    //查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
    private fun queryProvinces() {
        title_text.text = "中国"
        back_button.visibility = View.GONE
        provinceList = DbManager.getProvinces()
        if (provinceList.size > 0) {
            dataList.clear()
            dataList.addAll(provinceList.map { it.provinceName })
            adapter.notifyDataSetChanged()
            list_view.setSelection(0)
            currentLevel = LEVEL_PROVINCE
        } else {
            val address = "http://guolin.tech/api/china"
            queryFromServer(address, TYPE_PROVINCE)
        }
    }

    //查询选中省内的所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
    private fun queryCities() {
        title_text.text = selectedProvince.provinceName
        back_button.visibility = View.VISIBLE
        cityList = DbManager.queryCities(selectedProvince.provinceCode)
        if (cityList.size > 0) {
            dataList.clear()
            dataList.addAll(cityList.map { it.cityName })
            adapter.notifyDataSetChanged()
            list_view.setSelection(0)
            currentLevel = LEVEL_CITY
        } else {
            val provinceCode = selectedProvince.provinceCode
            var address = "http://guolin.tech/api/china/" + provinceCode
            queryFromServer(address, TYPE_CITY)
        }
    }

    //查询选中市内的所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
    private fun queryCounties() {
        title_text.text = selectedCity.cityName
        back_button.visibility = View.VISIBLE
        countyList = DbManager.queryCounties(selectedCity.cityCode)
        if (countyList.size > 0) {
            dataList.clear()
            dataList.addAll(countyList.map { it.countyName })
            adapter.notifyDataSetChanged()
            list_view.setSelection(0)
            currentLevel = LEVEL_COUNTY
        } else {
            var provinceCode = selectedProvince.provinceCode
            var cityCode = selectedCity.cityCode
            var address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode
            queryFromServer(address, TYPE_COUNTY)
        }
    }

    //根据传入的地址和类型从服务器上查询省、市、县数据
    private fun queryFromServer(address: String, type: String) {
        showProgressDialog()
        HttpUtil.sendOkHttpRequest(address, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    closeProgressDialog()
                    Toast.makeText(context, "加载失败", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val r = response.body()!!.string()
                var result: Boolean
                result = when (type) {
                    TYPE_PROVINCE -> HttpUtil.handleProvinceResponse(r)
                    TYPE_CITY -> HttpUtil.handleCityResponse(r, selectedProvince.provinceCode)
                    TYPE_COUNTY -> HttpUtil.handleCountyResponse(r, selectedCity.cityCode)
                    else -> false
                }
                if (result) {
                    activity?.runOnUiThread {
                        closeProgressDialog()
                        when (type) {
                            TYPE_PROVINCE -> queryProvinces()
                            TYPE_CITY -> queryCities()
                            TYPE_COUNTY -> queryCounties()
                        }
                    }
                }
            }
        })
    }

    fun showProgressDialog() {
        if (dialog == null) {
            dialog = ProgressDialog(context)
            dialog?.setMessage("正在加载...")
            dialog?.setCanceledOnTouchOutside(false)
        }
        dialog!!.show()
    }

    fun closeProgressDialog() {
        dialog?.dismiss()
    }

}