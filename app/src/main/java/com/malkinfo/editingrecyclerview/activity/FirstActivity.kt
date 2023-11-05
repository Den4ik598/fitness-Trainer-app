package com.malkinfo.editingrecyclerview.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malkinfo.editingrecyclerview.ExerciseAdapter
import com.malkinfo.editingrecyclerview.R
import com.malkinfo.editingrecyclerview.data.Exercise
import com.malkinfo.editingrecyclerview.data.WorkoutClass
import com.malkinfo.editingrecyclerview.databinding.ActivityMainBinding

class FirstActivity : AppCompatActivity() {

        private lateinit var recyclerView: RecyclerView
        private lateinit var adapter: ExerciseAdapter
        private val sharedPreferences by lazy { getSharedPreferences("MyPrefs", Context.MODE_PRIVATE) }

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
                    // add more default workouts if needed
                )
            }
            recyclerView = findViewById(R.id.mRecycler)
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = ExerciseAdapter(workoutList) // передаем список тренировок в адаптер
            recyclerView.adapter = adapter

            adapter.setOnItemClickListener { position ->
                val intent = Intent(this, TrainingActivity::class.java)
                intent.putExtra("workoutIndex", 0)
                intent.putExtra("exerciseListJson", Gson().toJson(workoutList[0].exercises))
                startActivity(intent)
            }

            val reminderWaterButton = findViewById<Button>(R.id.reminderWater)
            reminderWaterButton.setOnClickListener {
                val intent = Intent(this, WaterActivity::class.java)
                startActivity(intent)
            }

            val reminderCaloriesButton = findViewById<Button>(R.id.reminderCalorie)
            reminderCaloriesButton.setOnClickListener{
                val intent = Intent(this, CalorieActivity:: class.java)
                startActivity(intent)
            }
        }

        override fun onStop() {
            super.onStop()
            val workoutList = listOf(
                WorkoutClass(
                    "Workout 1",
                    listOf(
                        Exercise("Push-ups", "Place your hands shoulder-width apart on the floor. Lower your body until your chest nearly touches the floor. Push your body back up until your arms are fully extended.", 30, R.drawable.push_ups),
                        Exercise("Squats", "Stand with your feet shoulder-width apart. Lower your body as far as you can by pushing your hips back and bending your knees. Return to the starting position.", 45, R.drawable.squat),
                        Exercise("Plank", "Start in a push-up position, then bend your elbows and rest your weight on your forearms. Hold this position for as long as you can.", 60, R.drawable.planks)
                    )
                )
            )
            val json = Gson().toJson(workoutList)
            sharedPreferences.edit().putString("workoutListJson", json).apply()
        }
    }