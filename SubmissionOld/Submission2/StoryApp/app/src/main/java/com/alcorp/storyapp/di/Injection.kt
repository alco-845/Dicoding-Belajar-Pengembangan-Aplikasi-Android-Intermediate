package com.alcorp.storyapp.di

import com.alcorp.storyapp.api.ApiConfig
import com.alcorp.storyapp.data.StoryRepository

object Injection {
    fun provideRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService)
    }
}