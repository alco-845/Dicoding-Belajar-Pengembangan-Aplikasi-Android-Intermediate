package com.alcorp.storyapp.api

import com.alcorp.storyapp.model.DetailStory
import com.alcorp.storyapp.model.FileResponse
import com.alcorp.storyapp.model.ListStory
import com.alcorp.storyapp.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("v1/register")
    fun regisUser (
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<FileResponse>

    @FormUrlEncoded
    @POST("v1/login")
    fun loginUser (
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<User>

    @GET("v1/stories")
    suspend fun getListStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ListStory

    @GET("v1/stories")
    fun getStoryLocation(
        @Header("Authorization") token: String,
        @Query("location") page: Int,
    ): Call<ListStory>

    @Multipart
    @POST("v1/stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
    ): Call<FileResponse>

    @GET("v1/stories/{id}")
    fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DetailStory>
}