package com.alcorp.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alcorp.storyapp.data.AppRepository
import com.alcorp.storyapp.data.remote.response.DataResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddViewModel(private val repository: AppRepository) : ViewModel() {
    private val _storyResponse = MutableLiveData<DataResponse>()
    val storyResponse: LiveData<DataResponse> = _storyResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    val message = MutableLiveData<String>()

    fun addStory(token: String, description: RequestBody, file: MultipartBody.Part) {
        _isLoading.value = true
        val response: Call<DataResponse> = repository.addStory(token, description, file)
        response.enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _storyResponse.value = response.body()
                    message.value = response.body()!!.message
                } else {
                    message.value = response.message()
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                _isLoading.value = false
                message.value = t.message
            }
        })
    }
}