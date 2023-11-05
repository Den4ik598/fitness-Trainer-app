package com.malkinfo.editingrecyclerview.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malkinfo.editingrecyclerview.R
import com.malkinfo.editingrecyclerview.data.Exercise
import java.lang.String.format

class TrainingActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var exerciseTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var startButton: Button
    private lateinit var completeButton: Button
    private lateinit var imageView: ImageView
    private lateinit var exercises: List<Exercise>
    private var exerciseIndex = 0
    private lateinit var currentExercise: Exercise
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        val exerciseListJson = intent.getStringExtra("exerciseListJson")
        exercises = Gson().fromJson(exerciseListJson, object : TypeToken<List<Exercise>>() {}.type)

        titleTextView = findViewById(R.id.titleTextView)
        exerciseTextView = findViewById(R.id.exerciseTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        timerTextView = findViewById(R.id.timerTextView)
        startButton = findViewById(R.id.startButton)
        completeButton = findViewById(R.id.completeButton)
        imageView = findViewById(R.id.imageView)

        startButton.setOnClickListener {
            startWorkout()
        }

        completeButton.setOnClickListener {
            completeExercise()
        }
    }

    private fun completeExercise() {
        timer.cancel()
        completeButton.isEnabled = false
        startNextExercise()
    }

    private fun startWorkout() {
        exerciseIndex = 0
        titleTextView.text = "Workout Started"
        startButton.isEnabled = false
        startButton.text = "Workout In Progress"
        startNextExercise()
    }

    private fun startNextExercise() {
        if (exerciseIndex < exercises.size) {
            currentExercise = exercises[exerciseIndex]
            exerciseTextView.text = currentExercise.name
            descriptionTextView.text = currentExercise.description
            Glide.with(this@TrainingActivity)
                .load(currentExercise.gifImageUrl)
                .into(imageView)
            timerTextView.text = formatTime(currentExercise.durationInSeconds)

            timer = object : CountDownTimer(currentExercise.durationInSeconds * 1000L, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timerTextView.text = formatTime((millisUntilFinished / 1000).toInt())
                }

                override fun onFinish() {
                    timerTextView.text = "Exercise Complete"
                    imageView.visibility = View.VISIBLE
                    completeButton.isEnabled = true
                }
            }.start()

            exerciseIndex++
        } else {
            exerciseTextView.text = "Workout Complete"
            descriptionTextView.text = ""
            timerTextView.text = ""
            completeButton.isEnabled = false
            startButton.isEnabled = true
            startButton.text = "Start Again"
        }
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return format("%02d:%02d", minutes, remainingSeconds)
    }
}