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

class MainViewModel(private val repository: AppRepository) : ViewModel() {
    private val _listStory = MutableLiveData<List<ListStoryResponse>>()
    val listStory: LiveData<List<ListStoryResponse>> = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    val message = MutableLiveData<String>()

    fun getListStory(token: String) {
        _isLoading.value = true
        val response: Call<ListStory> = repository.getListStory(token)
        response.enqueue(object : Callback<ListStory> {
            override fun onResponse(call: Call<ListStory>, response: Response<ListStory>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listStory.value = response.body()!!.listStory
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