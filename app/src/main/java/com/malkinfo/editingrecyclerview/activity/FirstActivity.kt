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
import com.malkinfo.editingrecyclerview.data.WorkoutClass

class FirstActivity : AppCompatActivity() {

    companion object {
        const val ADD_TRAINING_REQUEST_CODE = 1
    }

    private lateinit var addsBtn: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WorkoutAdapter

    private val sharedPreferences by lazy {
        getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)

        val json: String? = sharedPreferences.getString("workoutListJson", null)
        val workoutList: List<WorkoutClass> = if (!json.isNullOrEmpty()) {
            Gson().fromJson(json, object : TypeToken<List<WorkoutClass>>() {}.type)
        } else {
            // create a default list when json is null or empty
            listOf(
                WorkoutClass("Workout 1", listOf())
            )
        }

        recyclerView = findViewById(R.id.mRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = WorkoutAdapter(workoutList.toMutableList())
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener { position ->
            if (position >= 0 && position < workoutList.size) {
                val intent = Intent(this, TrainingActivity::class.java)
                intent.putExtra("workoutIndex", position)
                intent.putExtra("exerciseListJson", Gson().toJson(workoutList[position].exercises))
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
            startActivityForResult(intent.apply {
                val json = Gson().toJson(adapter.workoutList)
                putExtra("workoutListJson", json)
            }, ADD_TRAINING_REQUEST_CODE)
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
            val workout = data?.getSerializableExtra("workout") as? WorkoutClass
            workout?.let { nonNullWorkout ->
                adapter.addWorkout(nonNullWorkout)
                val json = data.getStringExtra("workoutListJson")
                sharedPreferences.edit().putString("workoutListJson", json).apply()
                adapter.notifyDataSetChanged()
            }
        }
    }
}