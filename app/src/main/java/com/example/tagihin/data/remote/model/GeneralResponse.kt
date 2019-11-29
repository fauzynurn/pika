package com.example.tagihin.data.remote.model

class GeneralResponse(
    override var status: Boolean,
    override var message: String,
    override var data: Any?
) : BaseResponse<Any?>