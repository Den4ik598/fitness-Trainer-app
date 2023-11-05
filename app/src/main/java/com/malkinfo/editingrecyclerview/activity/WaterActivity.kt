package com.malkinfo.editingrecyclerview.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malkinfo.editingrecyclerview.R
import com.malkinfo.editingrecyclerview.WaterDrinkAdapter
import com.malkinfo.editingrecyclerview.data.Drink
import com.malkinfo.editingrecyclerview.data.UserData

@Suppress("CAST_NEVER_SUCCEEDS")
class WaterActivity : AppCompatActivity() {

    private lateinit var tvDailyGoal: TextView
    private lateinit var pbWaterProgress: ProgressBar
    private lateinit var tvDailyWater: TextView
    private lateinit var rvWaterDrinks: RecyclerView
    private lateinit var btnAddDrink: Button
    private lateinit var waterData: UserData
    private lateinit var drinks: MutableList<Drink>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water)

        tvDailyGoal = findViewById(R.id.tvDailyGoal)
        pbWaterProgress = findViewById(R.id.pbWaterProgress)
        tvDailyWater = findViewById(R.id.tvDailyWater)
        rvWaterDrinks = findViewById(R.id.rvWaterDrinks)
        btnAddDrink = findViewById(R.id.btnAddDrink)



        val getUserData = getSharedPreferences("UserData", MODE_PRIVATE)
        val jsonString = getUserData.getString("data","" )
        val gson = Gson()
        waterData = gson.fromJson(jsonString,UserData::class.java)

        drinks = mutableListOf()

        // загрузка данных из SharedPreferences
        loadWaterDataFromSharedPreferences()
        loadWaterDrinksFromSharedPreferences()

        // настройка кнопки добавления напитка
        btnAddDrink.setOnClickListener {
            val intent = Intent(this, AddDrinkActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_NEW_DRINK)
        }

        // настройка RecyclerView
        val adapter = WaterDrinkAdapter(drinks)
        rvWaterDrinks.adapter = adapter
        rvWaterDrinks.layoutManager = LinearLayoutManager(this)

        // обновление элементов интерфейса
        updateWaterProgressBar()
        updateWaterTextFields()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_NEW_DRINK) {
            val drink = data?.getParcelableExtra<Drink>("drink")!!
            addDrink(drink)
            updateWaterAmount(drink)
        }
    }

    fun addDrink(drink: Drink) {
        drinks.add(drink)
        saveWaterDrinksToSharedPreferences()
        rvWaterDrinks.adapter?.notifyDataSetChanged()
    }

    fun updateWaterAmount(drink: Drink) {
        waterData.wantersutk += drink.volume
        saveWaterDataToSharedPreferences()
        updateWaterProgressBar()
        updateWaterTextFields()
    }

    private fun updateWaterProgressBar() {
        pbWaterProgress.max = waterData.waterNorm.toInt()
        pbWaterProgress.progress = waterData.wantersutk.toInt()
    }

    private fun updateWaterTextFields() {
        tvDailyGoal.text = "Суточная норма: ${waterData.waterNorm} мл"
        tvDailyWater.text = "Выпито за сегодня: ${waterData.wantersutk} мл"
    }

    private fun loadWaterDataFromSharedPreferences() {
        val prefs = getSharedPreferences(PREF_FILE_USER_DATA, Context.MODE_PRIVATE)
        println(waterData.wantersutk)
        println(waterData)
        waterData.wantersutk = prefs.getInt(KEY_DAILY_WATER, 0).toDouble()
    }

    private fun saveWaterDataToSharedPreferences() {
        val prefs = getSharedPreferences(PREF_FILE_USER_DATA, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putInt(KEY_DAILY_WATER, waterData.wantersutk.toInt())
            println(waterData)
            println(waterData.wantersutk)
            apply()
        }
    }

    private fun loadWaterDrinksFromSharedPreferences() {
        drinks.clear()
        val prefs = getSharedPreferences(PREF_FILE_WATER_DRINKS, Context.MODE_PRIVATE)
        val json = prefs.getString(PREF_FILE_WATER_DRINKS, null)
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<Drink>>() {}.type
            val list = Gson().fromJson<MutableList<Drink>>(json, type)
            drinks.addAll(list)
        }
    }

    private fun saveWaterDrinksToSharedPreferences() {
        val prefs = getSharedPreferences(PREF_FILE_WATER_DRINKS, Context.MODE_PRIVATE)
        val json = Gson().toJson(drinks)
        with(prefs.edit()) {
            putString(PREF_FILE_WATER_DRINKS, json)
            apply()
        }
    }

    companion object {
        private const val PREF_FILE_USER_DATA = "data"
        private const val PREF_FILE_WATER_DRINKS = "water_drinks"
        private const val KEY_DAILY_GOAL = "daily_goal"
        private const val KEY_DAILY_WATER = "daily_water"
        private const val REQUEST_CODE_NEW_DRINK = 1
    }
}