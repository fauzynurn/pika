package com.example.tagihin.data.remote.model

class LoginResponse(
    override var status: Boolean,
    override var message: String,
    override var data: Login?
) : BaseResponse<Login>

data class Login(
    var id : Int,
    var nama : String,
    var foto : String
)
