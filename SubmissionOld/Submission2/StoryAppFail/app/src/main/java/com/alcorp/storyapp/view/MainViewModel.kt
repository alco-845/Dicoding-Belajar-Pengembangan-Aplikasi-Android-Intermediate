package com.alcorp.storyapp.view

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alcorp.storyapp.data.StoryRepository
import com.alcorp.storyapp.di.Injection
import com.alcorp.storyapp.model.ListStoryResponse

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
//    val story: LiveData<PagingData<ListStoryResponse>> =
//        storyRepository.getStory(token).cachedIn(viewModelScope)

    fun getStory(): LiveData<PagingData<ListStoryResponse>> {
        return storyRepository.getStory().cachedIn(viewModelScope)
    }
}