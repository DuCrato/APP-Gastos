package com.example.mypocket.model

data class Expense(

    var id      : Int = 0,
    var descrip : String = "",
    var value   : String = "",
    var date    : String = "",
    var parcel  : String = "",
    var paid    : String = ""
)