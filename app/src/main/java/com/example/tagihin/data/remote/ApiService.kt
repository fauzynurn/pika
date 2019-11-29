package com.example.tagihin.data.remote

import com.example.tagihin.data.remote.model.*
import io.reactivex.Maybe
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("Api/signin")
    fun login(
        @Field("username") username : String,
        @Field("password") password : String
        ): Maybe<Response<LoginResponse>>

    @FormUrlEncoded
    @POST("Api/data_tagihan_lunas")
    fun getPaidBill(
        @Field("username") username : String,
        @Field("level_user") levelUser : Int,
        @Field("page") page : Int,
        @Field("size") size : Int
    ): Maybe<Response<BillResponse>>

    @FormUrlEncoded
    @POST("Api/data_tagihan_belum")
    fun getUnpaidBill(
        @Field("username") username : String,
        @Field("level_user") levelUser : Int,
        @Field("page") page : Int,
        @Field("size") size : Int
    ): Maybe<Response<BillResponse>>

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
    ): Maybe<Response<BillResponse>>

    @FormUrlEncoded
    @POST("Api/jumlah_tagihan")
    fun getBillStat(
        @Field("username") username : String,
        @Field("level_user") levelUser : Int
    ): Maybe<Response<BillStatResponse>>
}