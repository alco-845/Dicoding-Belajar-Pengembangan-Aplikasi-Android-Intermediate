package com.alcorp.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.alcorp.storyapp.api.ApiService
import com.alcorp.storyapp.model.ListStory
import com.alcorp.storyapp.model.ListStoryResponse

class StoryRepository(private val token: String, private val apiService: ApiService) {
    fun getStory(): LiveData<PagingData<ListStoryResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(token, apiService)
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            token: String,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(token, apiService)
            }.also { instance = it }
    }
}