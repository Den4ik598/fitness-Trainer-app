package com.malkinfo.editingrecyclerview.activity

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
import com.malkinfo.editingrecyclerview.CaloriesAdapter
import com.malkinfo.editingrecyclerview.R
import com.malkinfo.editingrecyclerview.data.CaloriesData
import com.malkinfo.editingrecyclerview.data.UserData

class CalorieActivity : AppCompatActivity() {


    private lateinit var tvDailyGoal: TextView
    private lateinit var pbWaterProgress: ProgressBar
    private lateinit var tvDailyCalories: TextView
    private lateinit var rvWaterDishes: RecyclerView
    private lateinit var btnAddDrink: Button
    private lateinit var waterData: UserData
    private lateinit var dishes: MutableList<CaloriesData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dishes)

        tvDailyGoal = findViewById(R.id.tvDDailyGoal)
        pbWaterProgress = findViewById(R.id.pbCaloriesProgress)
        tvDailyCalories = findViewById(R.id.tvDailyCalories)
        rvWaterDishes = findViewById(R.id.rvCaloriesDrinks)
        btnAddDrink = findViewById(R.id.btnAddDishes)

        val getUserData = getSharedPreferences("UserData", MODE_PRIVATE)
        val jsonString = getUserData.getString("data", "")
        val gson = Gson()
        waterData = gson.fromJson(jsonString, UserData::class.java)

        dishes = mutableListOf()

        loadWaterDataFromSharedPreferences()
        loadWaterDrinksFromSharedPreferences()

        btnAddDrink.setOnClickListener {
            val intent = Intent(this, AddDishesActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_NEW_DRINK)
        }

        val adapter = CaloriesAdapter(this,dishes)
        adapter.setOnItemClickListener { position ->
            updateCaloriesAmount(dishes[position])
        }
        rvWaterDishes.adapter = adapter
        rvWaterDishes.layoutManager = LinearLayoutManager(this)

        updateWaterProgressBar()
        updateWaterTextFields()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_NEW_DRINK) {
            val drink = data?.getParcelableExtra<CaloriesData>("calories")!!
            addDishes(drink)
            updateCaloriesAmount(drink)
        }
    }

    fun addDishes(dish: CaloriesData) {
        dishes.add(dish)
        saveWaterDrinksToSharedPreferences()
        rvWaterDishes.adapter?.notifyDataSetChanged()
    }

    fun updateCaloriesAmount(dish: CaloriesData) {
        waterData.kallorisutk += dish.volume
        saveWaterDataToSharedPreferences()
        updateWaterProgressBar()
        updateWaterTextFields()
    }

    private fun updateWaterProgressBar() {
        pbWaterProgress.max = waterData.kalloriNorm.toInt()
        pbWaterProgress.progress = waterData.kallorisutk.toInt()
    }

    private fun updateWaterTextFields() {
        tvDailyGoal.text = "Суточная норма: ${waterData.kalloriNorm} мл"
        tvDailyCalories.text = "Выпито за сегодня: ${waterData.kallorisutk} мл"
    }

    private fun loadWaterDataFromSharedPreferences() {
        val prefs = getSharedPreferences(PREF_FILE_USER_DATA, Context.MODE_PRIVATE)
        waterData.kallorisutk = prefs.getInt(KEY_DAILY_WATER, 0).toDouble()
    }


     private fun saveWaterDataToSharedPreferences() {
        val prefs = getSharedPreferences(PREF_FILE_USER_DATA, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putInt(KEY_DAILY_WATER, waterData.kallorisutk.toInt())
            apply()
        }
    }

    private fun loadWaterDrinksFromSharedPreferences() {
        dishes.clear()
        val prefs = getSharedPreferences(PREF_FILE_WATER_DRINKS, Context.MODE_PRIVATE)
        val json = prefs.getString(PREF_FILE_WATER_DRINKS, null)
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<CaloriesData>>() {}.type
            val list = Gson().fromJson<MutableList<CaloriesData>>(json, type)
            dishes.addAll(list)
        }
    }

     fun saveWaterDrinksToSharedPreferences() {
        val prefs = getSharedPreferences(PREF_FILE_WATER_DRINKS, Context.MODE_PRIVATE)
        val json = Gson().toJson(dishes)
        with(prefs.edit()) {
            putString(PREF_FILE_WATER_DRINKS, json)
            apply()
        }
    }

    companion object {
        private const val PREF_FILE_USER_DATA = "data"
        private const val PREF_FILE_WATER_DRINKS = "Calories_drinks"
        private const val KEY_DAILY_GOAL = "daily_goal_calories"
        private const val KEY_DAILY_WATER = "daily_calories"
        private const val REQUEST_CODE_NEW_DRINK = 1
    }
}