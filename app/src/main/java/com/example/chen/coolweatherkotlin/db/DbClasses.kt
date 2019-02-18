package com.example.chen.coolweatherkotlin.db

/**
 * Coder : chenshuaiyu
 * Time : 2019/2/9 16:07
 */

data class Province(val provinceName: String, val provinceCode: Int)

data class City(val cityName: String, val cityCode: Int, val provinceId: Int)

data class County(val countyName: String, val weatherId: String, val cityId: Int)