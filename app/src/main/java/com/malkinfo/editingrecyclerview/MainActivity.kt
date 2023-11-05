package com.malkinfo.editingrecyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.gson.Gson
import com.malkinfo.editingrecyclerview.activity.FirstActivity
import com.malkinfo.editingrecyclerview.data.UserData


private const val LINKS_PREFS = "links_prefs"
private const val LINKS_KEY = "links_key"
private const val PREFS_NAME = "MyPrefs"
private const val IS_FIRST_RUN = "isFirstRun"

class MainActivity : AppCompatActivity() {
    private lateinit var radioGroupActivityLevel: RadioGroup
    private lateinit var etWeight: EditText
    private lateinit var etHeight: EditText
    private lateinit var radioMale: RadioButton
    private lateinit var btn_save: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        if (prefs.getBoolean(IS_FIRST_RUN, true)) {
            setContentView(R.layout.activity_main)
            radioGroupActivityLevel = findViewById(R.id.radio_group_activity_level)
            etWeight = findViewById(R.id.et_weight)
            etHeight = findViewById(R.id.et_height)
            radioMale = findViewById(R.id.radio_male)
            btn_save = findViewById(R.id.btn_save)

            btn_save.setOnClickListener {
                saveUserData()
                startActivity(Intent(this@MainActivity, FirstActivity::class.java))
                finish()
            }

            prefs.edit().putBoolean(IS_FIRST_RUN, false).apply()
        } else {
            startActivity(Intent(this, FirstActivity::class.java))
            finish()
        }
    }

    private fun saveUserData() {
        val userData = UserData(
            radioGroupActivityLevel.checkedRadioButtonId,
            etWeight.text.toString().toDouble(),
            etHeight.text.toString().toDouble(),
            0.0,
            0.0,
            0.0,
            0.0,
            if (radioMale.isChecked) "male" else "female"
        )
        userData.calculateNorms()
        println(userData)
        val jsonData = Gson().toJson(userData)
        println(jsonData)
        val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("data", jsonData)
        editor.apply()
    }
}


/*private lateinit var radioGroupActivityLevel: RadioGroup
    private lateinit var etWeight: EditText
    private lateinit var etHeight: EditText
    private lateinit var radioMale: RadioButton
    private lateinit var btn_save: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        radioGroupActivityLevel = findViewById(R.id.radio_group_activity_level)
        etWeight = findViewById(R.id.et_weight)
        etHeight = findViewById(R.id.et_height)
        radioMale = findViewById(R.id.radio_male)
        btn_save = findViewById(R.id.btn_save)

        btn_save.setOnClickListener {
            saveUserData()
            startActivity(Intent(this@MainActivity, EmptyActivity::class.java))
            finish()
        }
    }

    private fun saveUserData() {
        val userData = UserData(
            radioGroupActivityLevel.checkedRadioButtonId,
            etWeight.text.toString().toDouble(),
            etHeight.text.toString().toDouble(),
            0.0,
            0.0,
            if (radioMale.isChecked) "male" else "female"
        )
        userData.calculateNorms()
        val jsonData = Gson().toJson(userData)
        val sharedPreferences = getSharedPreferences("UserData", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("data", jsonData)
        editor.apply()
    }  */




