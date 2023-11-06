package com.malkinfo.editingrecyclerview.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malkinfo.editingrecyclerview.ExerciseAdapter
import com.malkinfo.editingrecyclerview.R
import com.malkinfo.editingrecyclerview.WorkoutAdapter
import com.malkinfo.editingrecyclerview.data.Exercise
import com.malkinfo.editingrecyclerview.data.WorkoutClass

class AddTrainingActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseAdapter
    private lateinit var adapter2: WorkoutAdapter
    private val exerciseList = mutableListOf<Exercise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_training)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val json = intent.getStringExtra("workoutListJson")
        val workoutList: List<WorkoutClass> = if (!json.isNullOrEmpty()) {
            Gson().fromJson(json, object : TypeToken<List<WorkoutClass>>() {}.type)
        } else {
            // create a default list when json is null or empty
            listOf(
                WorkoutClass("Workout 1", listOf())
            )
        }

        recyclerView = findViewById(R.id.exerciseRecyclerView)
        adapter = ExerciseAdapter(exerciseList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val addExerciseButton = findViewById<Button>(R.id.addExerciseButton)
        addExerciseButton.setOnClickListener {
            val nameEditText = findViewById<EditText>(R.id.nameEditText)
            val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
            val durationEditText = findViewById<EditText>(R.id.durationEditText)

            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val durationInSeconds = durationEditText.text.toString().toInt()
            val gifImageResId = R.drawable.squat

            val exercise = Exercise(name, description, durationInSeconds, gifImageResId)
            exerciseList.add(exercise)
            adapter.notifyDataSetChanged()

            nameEditText.text.clear()
            descriptionEditText.text.clear()
            durationEditText.text.clear()
        }

        val saveTrainingButton = findViewById<Button>(R.id.saveTrainingButton)
        saveTrainingButton.setOnClickListener {
            val titleEditText = findViewById<EditText>(R.id.titleEditText)
            val title = titleEditText.text.toString()

            val workout = WorkoutClass(title, exerciseList.toList())
            val intent = Intent().apply {
                putExtra("workout", workout)
                putExtra("workoutListJson", Gson().toJson(workoutList))
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val title = titleEditText.text.toString()

        val workout = WorkoutClass(title, exerciseList.toList())
        val intent = Intent().apply {
            putExtra("workout", workout)
            putExtra("workoutListJson", Gson().toJson(adapter2.workoutList))
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}