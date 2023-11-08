package com.malkinfo.editingrecyclerview.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malkinfo.editingrecyclerview.ExerciseAdapter
import com.malkinfo.editingrecyclerview.R
import com.malkinfo.editingrecyclerview.WorkoutAdapter
import com.malkinfo.editingrecyclerview.data.CaloriesData
import com.malkinfo.editingrecyclerview.data.WorkoutClass

class FirstActivity : AppCompatActivity() {

    companion object {
        const val ADD_TRAINING_REQUEST_CODE = 1
    }

    private lateinit var addsBtn: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WorkoutAdapter
    private lateinit var workoutlist: MutableList<WorkoutClass>

    private val sharedPreferences by lazy {
        getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)
        workoutlist = mutableListOf()
        val json: String? = sharedPreferences.getString("workoutListJson", null)
        loadTrainingDataFromSharedPreferences()

        recyclerView = findViewById(R.id.mRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = WorkoutAdapter(workoutlist)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener { position ->
            if (position >= 0 && position < workoutlist.size) {
                val intent = Intent(this, TrainingActivity::class.java)
                intent.putExtra("workoutIndex", position)
                intent.putExtra("exerciseListJson", Gson().toJson(workoutlist[position].exercises))
                startActivity(intent)
            }
        }

        val reminderWaterButton = findViewById<Button>(R.id.reminderWater)
        reminderWaterButton.setOnClickListener {
            val intent = Intent(this, WaterActivity::class.java)
            startActivity(intent)
        }

        val reminderCaloriesButton = findViewById<Button>(R.id.reminderCalorie)
        reminderCaloriesButton.setOnClickListener {
            val intent = Intent(this, CalorieActivity::class.java)
            startActivity(intent)
        }

        addsBtn = findViewById(R.id.addTraining)
        addsBtn.setOnClickListener {
            val intent = Intent(this, AddTrainingActivity::class.java)
            startActivityForResult(intent, ADD_TRAINING_REQUEST_CODE)
        }
    }

    fun addTraining(Training: WorkoutClass){
        workoutlist.add(Training)
        saveTrainingDataToSharedPreferences()
        recyclerView.adapter?.notifyDataSetChanged()
    }

    fun saveTrainingDataToSharedPreferences(){
        val json =  Gson().toJson(workoutlist)
        with(sharedPreferences.edit()){
            putString("workoutListJson", json)
            apply()
        }

    }


    override fun onStop() {
        super.onStop()
        val json = Gson().toJson(adapter.workoutList)
        sharedPreferences.edit().putString("workoutListJson", json).apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TRAINING_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val Training = data?.getSerializableExtra("workoutListJson")!!
            addTraining(Training as WorkoutClass)

        }
//        if (requestCode == ADD_TRAINING_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val workout = data?.getSerializableExtra("workout") as? WorkoutClass
//            workout?.let { nonNullWorkout ->
//                adapter.addWorkout(nonNullWorkout)
//                val json = data.getStringExtra("workoutListJson")
//                sharedPreferences.edit().putString("workoutListJson", json).apply()
//                adapter.notifyDataSetChanged()
//                onStop()
//            }
//        }
    }


    private fun loadTrainingDataFromSharedPreferences(){
        workoutlist.clear()

        val json = sharedPreferences.getString("workoutListJson", null)
        if (!json.isNullOrEmpty()){
            val type = object : TypeToken<MutableList<WorkoutClass>>() {}.type
            val list = Gson().fromJson<MutableList<WorkoutClass>>(json,type)
            workoutlist.addAll(list)
        }


    }

}