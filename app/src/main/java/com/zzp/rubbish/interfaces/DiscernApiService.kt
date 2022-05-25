package com.zzp.rubbish.interfaces

import com.zzp.rubbish.data.ImageResponse
import com.zzp.rubbish.data.PostImageUrlBody
import retrofit2.Call
import retrofit2.http.*

interface DiscernApiService {
    @POST("/classifiedPictures")
    fun postImageUrl(@Body postImageUrlBody: PostImageUrlBody): Call<ImageResponse>
}