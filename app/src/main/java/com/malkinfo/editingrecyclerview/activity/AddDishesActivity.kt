package com.malkinfo.editingrecyclerview.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.malkinfo.editingrecyclerview.R
import com.malkinfo.editingrecyclerview.data.CaloriesData
import com.malkinfo.editingrecyclerview.data.Drink

class AddDishesActivity : AppCompatActivity(){

    private lateinit var etName: EditText
    private lateinit var etVolume: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dishes)

        etName = findViewById(R.id.detName)
        etVolume = findViewById(R.id.DetVolume)

        findViewById<Button>(R.id.btnAddDishes).setOnClickListener {
            val name = etName.text.toString()
            val volume = etVolume.text.toString().toIntOrNull()
            println(name)
            println(volume)
            if (name.isNotEmpty() && volume != null && volume > 0) {
                val drink = CaloriesData(
                    (1..1000).random(),
                    name,
                    volume
                )

                println(drink.toString())
                val intent = Intent()
                intent.putExtra("calories", drink)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Пожалуйста, введите название и объем напитка",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}