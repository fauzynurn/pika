package com.example.tagihin.data.remote.model

class BillStatResponse(
    override var status: Boolean,
    override var message: String,
    override var data: BillStat?
) : BaseResponse<BillStat>

data class BillStat(
    var lunas : Int,
    var pending : Int,
    var belum : Int,
    var seluruh : Int
)