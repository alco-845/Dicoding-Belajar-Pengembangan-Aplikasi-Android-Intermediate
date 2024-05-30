package com.alcorp.storyapp.di

import android.content.Context
import com.alcorp.storyapp.data.AppRepository
import com.alcorp.storyapp.data.local.room.AppDatabase
import com.alcorp.storyapp.data.remote.retrofit.ApiConfig


object Injection {
    fun provideRepository(context: Context): AppRepository {
        val database = AppDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return AppRepository.getInstance(database, apiService)
    }
}