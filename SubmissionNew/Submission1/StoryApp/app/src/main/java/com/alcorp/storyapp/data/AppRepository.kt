package com.alcorp.storyapp.data

import com.alcorp.storyapp.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AppRepository(private val apiService: ApiService) {
    fun regisUser(name: String, email: String, password: String) = apiService.regisUser(name, email, password)
    fun loginUser(email: String, password: String) = apiService.loginUser(email, password)
    fun getListStory(token: String) = apiService.getListStory(token)
    fun addStory(token: String, description: RequestBody, file: MultipartBody.Part) = apiService.addStory(token, description, file)
    fun getDetailStory(token: String, id: String) = apiService.getDetailStory(token, id)

    companion object {
        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            apiService: ApiService
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(apiService)
            }.also { instance = it }
    }
}