package com.example.tagihin.data.remote.model

import com.google.gson.annotations.SerializedName

interface BaseResponse<T> {
    var status: Boolean
    var message: String
    var data: T?
}