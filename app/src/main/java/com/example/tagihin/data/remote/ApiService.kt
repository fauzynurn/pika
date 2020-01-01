package com.example.tagihin.data.remote

import com.example.tagihin.data.remote.model.*
import io.reactivex.Maybe
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @FormUrlEncoded
    @POST("Api/signin")
    fun login(
        @Field("username") username : String,
        @Field("password") password : String
        ): Maybe<Response<LoginResponse>>

    @POST("Api/ubahWoOffline")
    fun sendSavedRequest(
        @Body body : SavedBillRequestBody
    ) : Maybe<Response<GeneralResponse>>


    @FormUrlEncoded
    @POST("Api/buat_WO")
    fun moveToWO(
        @Field("username") username : String,
        @Field("level_user") levelUser : Int,
        @FieldMap request : HashMap<String,Int>
    ): Maybe<Response<GeneralResponse>>

    @FormUrlEncoded
    @POST("Api/reset_WO")
    fun resetWo(
        @Field("username") username : String,
        @Field("level_user") levelUser : Int
    ): Maybe<Response<GeneralResponse>>

    @FormUrlEncoded
    @POST("Api/reset_tagihan")
    fun resetBill(
        @Field("username") username : String,
        @Field("level_user") levelUser : Int
    ): Maybe<Response<GeneralResponse>>

    @FormUrlEncoded
    @POST("Api/searching")
    fun searchUnpaid(
        @Field("username") username : String,
        @Field("query") query : String,
        @Field("type") type : String,
        @Field("page") page : Int,
        @Field("size") size : Int
    ): Maybe<Response<BillResponse<List<UnpaidBill>>>>

    @Multipart
    @POST("Api/upload_konfirmasi")
    fun uploadConfirmation(
        @Part("username") username : RequestBody,
        @Part("level_user") levelUser : RequestBody,
        @Part("id") id : RequestBody,
        @Part("idpel") idpel : RequestBody,
        @Part("nama") nama : RequestBody,
        @Part("alamat") alamat : RequestBody,
        @Part("tarif") tarif : RequestBody,
        @Part("daya") daya : RequestBody,
        @Part("tanggal") tanggal : RequestBody,
        @Part("meter_rusak") meterRusak : RequestBody,
        @Part("meter_siaga") meterSiaga : RequestBody,
        @Part("pasang_siaga") pasangSiaga : RequestBody,
        @Part("no_hp") noHp : RequestBody,
        @Part("kor_x") korX : RequestBody,
        @Part("kor_y") korY : RequestBody,
        @Part foto_siaga: MultipartBody.Part,
        @Part foto_rusak: MultipartBody.Part,
        @Part foto_bangunan: MultipartBody.Part
    ): Maybe<Response<GeneralResponse>>

    @FormUrlEncoded
    @POST("Api/upload_validasi")
    fun validateDil(
        @Field("username") username : String,
        @Field("level_user") levelUser : Int,
        @Field("id") id : Int,
        @Field("tanggal_validasi") tanggalValidasi: String,
        @Field("cabut_siaga") cabutSiaga : Int,
        @Field("jumlah_kwh") jumlahKwh : Int,
        @Field("tagihan") tagihan : Int,
        @Field("tarif") tarif : Int
    ) : Maybe<Response<GeneralResponse>>
    @FormUrlEncoded
    @POST("Api/searching")
    fun searchPending(
        @Field("username") username : String,
        @Field("query") query : String,
        @Field("type") type : String,
        @Field("page") page : Int,
        @Field("size") size : Int
    ): Maybe<Response<BillResponse<List<PendingBill>>>>

    @FormUrlEncoded
    @POST("Api/cari_dil")
    fun searchDil(
        @Field("username") username : String,
        @Field("query") query : String,
        @Field("level_user") levelUser : Int
    ): Maybe<Response<DilResponse>>

    @FormUrlEncoded
    @POST("Api/searching")
    fun searchPaid(
        @Field("username") username : String,
        @Field("query") query : String,
        @Field("type") type : String,
        @Field("page") page : Int,
        @Field("size") size : Int
    ): Maybe<Response<BillResponse<List<PaidBill>>>>

    @FormUrlEncoded
    @POST("Api/data_tagihan_lunas")
    fun getPaidBill(
        @Field("username") username : String,
        @Field("level_user") levelUser : Int,
        @Field("page") page : Int,
        @Field("size") size : Int
    ): Maybe<Response<BillResponse<MutableList<PaidBill>>>>

    @FormUrlEncoded
    @POST("Api/data_tagihan_belum")
    fun getUnpaidBill(
        @Field("username") username : String,
        @Field("level_user") levelUser : Int,
        @Field("page") page : Int,
        @Field("size") size : Int
    ): Maybe<Response<BillResponse<MutableList<UnpaidBill>>>>

    @FormUrlEncoded
    @POST("Api/update_status")
    fun updateBill(
        @Field("username") username : String,
        @Field("id") id : Int,
        @Field("status1") status : String,
        @Field("date") date : String,
        @Field("note") note : String
    ): Maybe<Response<GeneralResponse>>

    @FormUrlEncoded
    @POST("Api/data_tagihan_pending")
    fun getPendingBill(
        @Field("username") username : String,
        @Field("level_user") levelUser : Int,
        @Field("page") page : Int,
        @Field("size") size : Int
    ): Maybe<Response<BillResponse<MutableList<PendingBill>>>>

    @FormUrlEncoded
    @POST("Api/data_wo_pending")
    fun getPendingWorkOrderBill(
        @Field("username") username : String,
        @Field("level_user") levelUser : Int,
        @Field("page") page : Int,
        @Field("size") size : Int
    ): Maybe<Response<BillResponse<List<PendingWorkOrder>>>>

    @FormUrlEncoded
    @POST("Api/data_wo_belum")
    fun getUnpaidWorkOrderBill(
        @Field("username") username : String,
        @Field("level_user") levelUser : Int,
        @Field("page") page : Int,
        @Field("size") size : Int
    ): Maybe<Response<BillResponse<List<UnpaidWorkOrder>>>>

    @FormUrlEncoded
    @POST("Api/jumlah_tagihan")
    fun getBillStat(
        @Field("username") username : String,
        @Field("level_user") levelUser : Int
    ): Maybe<Response<BillStatResponse>>
}