package com.malkinfo.editingrecyclerview.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malkinfo.editingrecyclerview.ExerciseAdapter
import com.malkinfo.editingrecyclerview.R
import com.malkinfo.editingrecyclerview.data.Exercise
import com.malkinfo.editingrecyclerview.databinding.ActivityMainBinding

class FirstActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseAdapter
    private val exercises = mutableListOf<Exercise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)
        recyclerView = findViewById(R.id.mRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ExerciseAdapter(exercises)
        recyclerView.adapter = adapter





        adapter.setOnItemClickListener { position ->
            val intent = Intent(this, TrainingActivity::class.java)
            intent.putExtra("exerciseIndex", position)
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
}