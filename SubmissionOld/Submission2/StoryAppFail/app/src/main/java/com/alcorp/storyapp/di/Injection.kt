package com.alcorp.storyapp.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.alcorp.storyapp.api.ApiConfig
import com.alcorp.storyapp.data.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = context.getSharedPreferences("storyApp", MODE_PRIVATE)
        val token = pref.getString("token", "").toString()
        Log.d("test token", token)

        val apiService = ApiConfig.getRetrofit()
        return StoryRepository.getInstance(token, apiService)
    }
}