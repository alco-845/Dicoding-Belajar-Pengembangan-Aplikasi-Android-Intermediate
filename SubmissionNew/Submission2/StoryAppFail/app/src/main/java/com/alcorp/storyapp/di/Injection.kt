package com.alcorp.storyapp.di

import com.alcorp.storyapp.data.AppRepository
import com.alcorp.storyapp.data.remote.retrofit.ApiConfig


object Injection {
    fun provideRepository(): AppRepository {
        val apiService = ApiConfig.getApiService()
        return AppRepository.getInstance(apiService)
    }
}