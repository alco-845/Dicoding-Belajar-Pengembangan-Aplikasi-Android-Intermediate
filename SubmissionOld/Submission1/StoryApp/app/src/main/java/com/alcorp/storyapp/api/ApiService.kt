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
    @POST("register")
    fun regisUser (
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<FileResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser (
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<User>

    @GET("stories")
    fun getListStory(
        @Header("Authorization") token: String
    ): Call<ListStory>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
    ): Call<FileResponse>

    @GET("stories/{id}")
    fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DetailStory>
}