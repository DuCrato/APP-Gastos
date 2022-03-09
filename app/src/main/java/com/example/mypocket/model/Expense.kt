package com.example.mypocket.model

data class Expense(

    val id: Int = 0,
    val descrip: String,
    val value: String,
    val date: String,
    val parcel: Int = 1
)