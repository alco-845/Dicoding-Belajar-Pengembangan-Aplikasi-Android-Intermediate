package com.alcorp.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alcorp.storyapp.data.StoryRepository
import com.alcorp.storyapp.model.ListStoryResponse

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStory(): LiveData<PagingData<ListStoryResponse>> =
        storyRepository.getStory().cachedIn(viewModelScope)
}