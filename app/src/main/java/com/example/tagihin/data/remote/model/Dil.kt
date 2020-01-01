package com.example.tagihin.data.remote.model

import android.graphics.Bitmap

data class DilResponse (
    override var status: Boolean,
    override var message: String,
    override var data: DilItemResponse?
) : BaseResponse<DilItemResponse>

data class DilItemResponse (
    var id : String = "",
    var idpel : String = "",
    var nama : String = "",
    var alamat : String = "",
    var tarif : String = "",
    var daya : String = "",
    var gardu : String = "",
    var merek_kwh : String = "",
    var thtera_kwh : String = "",
    var nomor_kwh : String = "",
    var kor_x : String = "",
    var kor_y : String = "",
    var no_hp : String = "",
    var tanggal : String = "",
    var meter_rusak : String = "",
    var meter_siaga : String = "",
    var pasang_siaga : Int = 0,
    var x_upload : String = "",
    var y_upload : String = "",
    var foto_siaga : String? = null,
    var foto_rusak : String? = null,
    var foto_bangunan : String? = null
)

data class DilItemRequest (
    var id : String = "",
    var idpel : String = "",
    var nama : String = "",
    var alamat : String = "",
    var tarif : String = "",
    var daya : String = "",
    var gardu : String = "",
    var merek_kwh : String = "",
    var thtera_kwh : String = "",
    var nomor_kwh : String = "",
    var kor_x : String = "",
    var kor_y : String = "",
    var no_hp : String = "",
    var tanggal : String = "",
    var meter_rusak : String = "",
    var meter_siaga : String = "",
    var pasang_siaga : Int = 0,
    var x_upload : String = "",
    var y_upload : String = "",
    var foto_siaga : Bitmap? = null,
    var foto_rusak : Bitmap? = null,
    var foto_bangunan : Bitmap? = null
)

data class DilItemValidationRequest(
    var id : Int = -1,
    var tanggal_validasi : String = "",
    var cabut_siaga : Int = 0,
    var jumlah_kwh : Int = 0,
    var tagihan : Int = 0,
    var tarif : Int = 0
)