package com.example.chen.coolweatherkotlin.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

/**
 * Coder : chenshuaiyu
 * Time : 2019/2/14 21:23
 */
object DbManager {
    private val db: DbHelper = DbHelper()
    private val sqLiteDatabase: SQLiteDatabase = db.writableDatabase

    fun getProvinces(): List<Province> {
        val cursor: Cursor = sqLiteDatabase.query("Province", null, null, null, null, null, null)
        val list: ArrayList<Province> = ArrayList()
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Province(
                        cursor.getString(cursor.getColumnIndex("provinceName")),
                        cursor.getInt(cursor.getColumnIndex("provinceCode"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun queryCities(provinceId: Int): List<City> {
        val cursor: Cursor =
            sqLiteDatabase.query("City", null, "provinceId = ?", arrayOf("" + provinceId), null, null, null)
        val list: ArrayList<City> = ArrayList()
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    City(
                        cursor.getString(cursor.getColumnIndex("cityName")),
                        cursor.getInt(cursor.getColumnIndex("cityCode")),
                        cursor.getInt(cursor.getColumnIndex("provinceId"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun queryCounties(cityId: Int): List<County> {
        val cursor: Cursor = sqLiteDatabase.query("County", null, "cityId = ?", arrayOf("" + cityId), null, null, null)
        val list: ArrayList<County> = ArrayList()
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    County(
                        cursor.getString(cursor.getColumnIndex("countyName")),
                        cursor.getString(cursor.getColumnIndex("weatherId")),
                        cursor.getInt(cursor.getColumnIndex("cityId"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun addProvince(province: Province) {
        val contentValues = ContentValues()
        contentValues.put("provinceName", province.provinceName)
        contentValues.put("provinceCode", province.provinceCode)
        sqLiteDatabase.insert("Province", null, contentValues)
    }

    fun addCity(city: City) {
        val contentValues = ContentValues()
        contentValues.put("cityName", city.cityName)
        contentValues.put("cityCode", city.cityCode)
        contentValues.put("provinceId", city.provinceId)
        sqLiteDatabase.insert("City", null, contentValues)
    }

    fun addCounty(county: County) {
        val contentValues = ContentValues()
        contentValues.put("countyName", county.countyName)
        contentValues.put("weatherId", county.weatherId)
        contentValues.put("cityId", county.cityId)
        sqLiteDatabase.insert("County", null, contentValues)
    }
}