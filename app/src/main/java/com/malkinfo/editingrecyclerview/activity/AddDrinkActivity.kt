package com.malkinfo.editingrecyclerview.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.malkinfo.editingrecyclerview.R
import com.malkinfo.editingrecyclerview.data.Drink

class AddDrinkActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etVolume: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_drink)

        etName = findViewById(R.id.etName)
        etVolume = findViewById(R.id.etVolume)

        findViewById<Button>(R.id.btnAddDrink).setOnClickListener {
            val name = etName.text.toString()
            val volume = etVolume.text.toString().toIntOrNull()
            if (name.isNotEmpty() && volume != null && volume > 0) {
                val drink = Drink(
                    (1..1000).random(),
                    name,
                    volume
                )

                println(drink.toString())
                val intent = Intent()
                intent.putExtra("drink", drink)
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