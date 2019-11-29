package com.example.tagihin.data.remote.model

data class BillRequest (
    var page : Int,
    var size : Int,
    var username : String,
    var level_user : Int
)
