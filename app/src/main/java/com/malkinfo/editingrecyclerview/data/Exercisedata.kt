package com.malkinfo.editingrecyclerview.data

import java.io.Serializable

data class Exercise(
    val name: String,
    val description: String,
    val durationInSeconds: Int ,
    val gifImageUrl : Int
) : Serializable
