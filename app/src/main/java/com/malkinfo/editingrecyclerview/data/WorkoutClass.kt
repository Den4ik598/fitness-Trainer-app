package com.malkinfo.editingrecyclerview.data

import java.io.Serializable

data class WorkoutClass(
    val title: String,
    val exercises: List<Exercise> = listOf()
) : Serializable