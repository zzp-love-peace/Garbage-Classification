package com.zzp.rubbish.interfaces

import com.zzp.rubbish.data.ImageResult
import com.zzp.rubbish.data.PostImageUrlBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("/classifiedPictures")
    fun postImageUrl(@Body postImageUrlBody: PostImageUrlBody): Call<ImageResult>
}