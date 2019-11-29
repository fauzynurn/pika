package com.example.tagihin.data.remote.model

import java.io.Serializable

class BillResponse(
    override var status: Boolean,
    override var message: String,
    override var data: List<Bill>?
) : BaseResponse<List<Bill>>

data class Bill(
    var id : Int,
    var idpel : String,
    var nama : String,
    var alamat : String,
    var rbm : String,
    var total : String,
    var tanggal : String?,
    var keterangan : String,
    var tgl_pending : String?,
    var catatan : String?
) : Serializable