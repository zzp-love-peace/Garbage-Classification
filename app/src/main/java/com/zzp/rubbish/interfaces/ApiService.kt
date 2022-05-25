package com.zzp.rubbish.interfaces

import com.zzp.rubbish.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface ApiService {
    @GET("getCode")
    fun getCodeOfRegister(@Query("phoneNumber") phoneNumber: String): Call<NormalResponse>

    @GET("sendCode")
    fun getCodeOfLogin(@Query("phoneNumber") phoneNumber: String): Call<NormalResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("userName") userName: String,
        @Field("password") password: String,
        @Field("code") code: String
    ): Call<NormalResponse>

    @POST("loginByPassword")
    fun loginByPwd(@Body loginByPwdBody: LoginByPwdBody): Call<LoginResponse>

    @FormUrlEncoded
    @POST("loginByCode")
    fun loginByCode(
        @Field("phoneNumber") phoneNumber: String,
        @Field("code") code: String
    ): Call<LoginResponse>

    // 获取总积分
    @GET("data/getTotalIntegral")
    fun getTotalIntegral(@Header("Authorization")token: String): Call<GetNumResponse>

    // 获取总回收数量
    @GET("data/getTotalRecoveryOrder")
    fun getTotalRecoveryOrder(@Header("Authorization")token: String): Call<GetNumResponse>

    // 获取总兑换数量
    @GET("data/getTotalExchangeOrder")
    fun getTotalExchangeOrder(@Header("Authorization")token: String): Call<GetNumResponse>

    @GET("policy/getAllCollectPolicy")
    fun getAllCollectPolicy(@Header("Authorization")token: String): Call<GetAllCollectPolicyResponse>

    @GET("goods/getGoods")
    fun getGoods(@Header("Authorization")token: String): Call<GetGoodsResponse>

    @GET("policy/getPolicy")
    fun getPolicy(@Header("Authorization")token: String): Call<GetPolicyResponse>
}