package com.alcorp.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alcorp.storyapp.data.AppRepository
import com.alcorp.storyapp.data.remote.response.ListStory
import com.alcorp.storyapp.data.remote.response.ListStoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val repository: AppRepository) : ViewModel() {
    private val _storyMaps = MutableLiveData<List<ListStoryResponse>>()
    val storyMaps: LiveData<List<ListStoryResponse>> = _storyMaps

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    val message = MutableLiveData<String>()

    fun getStoryMaps(token: String, location: Int) {
        _isLoading.value = true
        val response: Call<ListStory> = repository.getStoryLocation(token, location)
        response.enqueue(object : Callback<ListStory> {
            override fun onResponse(call: Call<ListStory>, response: Response<ListStory>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _storyMaps.value = response.body()!!.listStory
                    message.value = response.body()!!.message
                } else {
                    message.value = response.message()
                }
            }

            override fun onFailure(call: Call<ListStory>, t: Throwable) {
                _isLoading.value = false
                message.value = t.message
            }
        })
    }
}