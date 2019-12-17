package com.example.tagihin.data.remote

import com.example.tagihin.data.remote.model.*
import io.reactivex.Maybe
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