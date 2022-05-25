package com.zzp.rubbish.data

data class PostImageUrlBody(val imageUrl: String, val token: String)

data class ImageResponse(val code: Int, val data: String)

data class NormalResponse(val code: Int, val msg: String, val data: Any)

data class LoginByPwdBody(val userName: String, val password: String)

data class LoginResponse(val code: Int, val msg: String, val data: LoginData)

data class LoginData(val token: String, val id: Int)

data class GetNumResponse(val code: Int, val msg: String, val data: GetNumData)

data class GetNumData(val data: Int)

data class GetAllCollectPolicyResponse(val code: Int, val msg: String, val data: CollectPolicyData)

data class CollectPolicyData(val collects: List<Int>)

data class GetGoodsResponse(val code: Int, val msg: String, val data: GoodsData)

data class GoodsData(val goods: List<Goods>)

data class Goods(
    val id: Int,
    val name: String,
    val image: String,
    val integral: Int,
    val price: Int
)

data class GetPolicyResponse(val code: Int, val msg: String, val data: PolicyData)

data class PolicyData(val policies: List<Policy>)

data class Policy(
    val id: Int,
    val image: String,
    val title: String,
    val author: String,
    val content: String,
    val createDate: String
)