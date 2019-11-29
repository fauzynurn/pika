package com.example.tagihin.data.remote.model

import com.google.gson.annotations.SerializedName

data class SampleResponse(
    var userId : Int,
    var id : Int,
    var title : String,
    var completed : Boolean
)
