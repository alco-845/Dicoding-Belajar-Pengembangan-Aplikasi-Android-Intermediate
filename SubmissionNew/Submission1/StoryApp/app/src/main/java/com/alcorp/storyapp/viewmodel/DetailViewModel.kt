package com.alcorp.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alcorp.storyapp.data.AppRepository
import com.alcorp.storyapp.data.remote.response.DetailStory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val repository: AppRepository) : ViewModel() {
    private val _detailStory = MutableLiveData<DetailStory>()
    val detailStory: LiveData<DetailStory> = _detailStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    val message = MutableLiveData<String>()

    fun getDetailStory(token: String, id: String) {
        _isLoading.value = true
        val response: Call<DetailStory> = repository.getDetailStory(token, id)
        response.enqueue(object : Callback<DetailStory> {
            override fun onResponse(call: Call<DetailStory>, response: Response<DetailStory>) {
                _isLoading.value = false
                _detailStory.value = response.body()
            }

            override fun onFailure(call: Call<DetailStory>, t: Throwable) {
                _isLoading.value = false
                message.value = t.message
            }
        })
    }
}