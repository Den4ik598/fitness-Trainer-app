package com.malkinfo.editingrecyclerview.model

data class ClassLink(
    val Id: Int,
    val Protocol: String,
    val Appname: String,
    val Uuid: String,
    val Adress: String,
    val Port: Int,
    val Security: String,
    val Sni:String,
    val Public_Key: String,
    val Browser: String,
    val Type:String,
    val Flow:String,
    val Country:String
)