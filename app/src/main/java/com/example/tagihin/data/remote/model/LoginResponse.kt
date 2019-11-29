package com.example.tagihin.data.remote.model

import com.google.gson.annotations.SerializedName

class LoginResponse(
    override var status: Boolean,
    override var message: String,
    override var data: Login?
) : BaseResponse<Login>

data class Login(
    var id : Int,
    var nama : String
)
