package com.zzp.rubbish.network

import com.zzp.rubbish.data.LoginByPwdBody
import com.zzp.rubbish.interfaces.DiscernApiService
import com.zzp.rubbish.data.PostImageUrlBody
import com.zzp.rubbish.interfaces.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object NetWork {
    private val discernApiService = RetrofitManager.createDiscern<DiscernApiService>()
    private val apiService = RetrofitManager.create<ApiService>()

    suspend fun postImageUrl(imageUrl: String, token: String) = discernApiService.postImageUrl(
        PostImageUrlBody(imageUrl, token)
    ).await()

    suspend fun getCodeOfRegister(phoneNum: String) = apiService.getCodeOfRegister(phoneNum).await()
    suspend fun getCodeOfLogin(phoneNum: String) = apiService.getCodeOfLogin(phoneNum).await()
    suspend fun register(phoneNum: String, pwd: String, code: String) =
        apiService.register(phoneNum, pwd, code).await()

    suspend fun loginByPwd(phoneNum: String, pwd: String) =
        apiService.loginByPwd(LoginByPwdBody(phoneNum, pwd)).await()

    suspend fun loginByCode(phoneNum: String, code: String) =
        apiService.loginByCode(phoneNum, code).await()
    suspend fun getTotalRecoveryOrder(token: String) = apiService.getTotalIntegral(token).await()
    suspend fun getTotalIntegral(token: String) = apiService.getTotalRecoveryOrder(token).await()
    suspend fun getTotalExchangeOrder(token: String) = apiService.getTotalExchangeOrder(token).await()
    suspend fun getGoods(token: String) = apiService.getGoods(token).await()
    suspend fun getPolicy(token: String) = apiService.getPolicy(token).await()

    //  给网络请求方法的返回值增加扩展函数await，简化回调
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}