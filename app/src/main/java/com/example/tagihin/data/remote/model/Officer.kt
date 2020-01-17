package com.example.tagihin.data.remote.model

import com.google.gson.annotations.SerializedName

data class OfficerList (
    @SerializedName("petugas")
    var list : MutableList<Officer> = mutableListOf()
)
data class Officer(
    var username : String = "",
    var foto : String = ""
)