package com.alcorp.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alcorp.storyapp.data.AppRepository
import com.alcorp.storyapp.data.remote.response.ListStoryResponse

class MainViewModel(private val repository: AppRepository) : ViewModel() {
    fun getListStory(): LiveData<PagingData<ListStoryResponse>> =
        repository.getListStory().cachedIn(viewModelScope)
}