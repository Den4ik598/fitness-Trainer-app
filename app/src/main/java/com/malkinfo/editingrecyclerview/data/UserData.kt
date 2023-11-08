package com.malkinfo.editingrecyclerview.data

import com.malkinfo.editingrecyclerview.R


data class UserData(
    var activityLevel: Int,
    var weight: Double,
    var height: Double,
    var waterNorm: Double,
    var kalloriNorm: Double,
    var kallorisutk: Double,
    var wantersutk: Double,
    var sex: String,

) {
    init {
        calculateNorms()
    }

    public fun calculateNorms() {
        val age = 25
        val sexMult = if (sex == "male") 1.0 else -161.0
        val kalloriCoeff = when(activityLevel) {
            R.id.radio_level_1 -> 1.2
            R.id.radio_level_2 -> 1.375
            R.id.radio_level_3 -> 1.55
            R.id.radio_level_4 -> 1.725
            R.id.radio_level_5 -> 1.9
            else -> 1.2
        }
        kalloriNorm = ((weight * 10) + (height * 6.25) - (age * 5) + sexMult) * kalloriCoeff
        waterNorm = weight * if (sex == "male") 35 else 31
    }
}