package com.alcorp.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.alcorp.storyapp.data.remote.response.ListStoryResponse
import com.alcorp.storyapp.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AppRepository(private val apiService: ApiService) {
    fun regisUser(name: String, email: String, password: String) = apiService.regisUser(name, email, password)
    fun loginUser(email: String, password: String) = apiService.loginUser(email, password)

    fun getListStory(): LiveData<PagingData<ListStoryResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

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