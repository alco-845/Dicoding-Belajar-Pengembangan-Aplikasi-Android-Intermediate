package com.alcorp.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alcorp.storyapp.data.AppRepository
import com.alcorp.storyapp.data.remote.response.DataResponse
import com.alcorp.storyapp.data.remote.response.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val repository: AppRepository) : ViewModel() {
    private val _regisUser = MutableLiveData<String>()
    val regisUser: LiveData<String> = _regisUser

    private val _loginUser = MutableLiveData<User>()
    val loginUser: LiveData<User> = _loginUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    val message = MutableLiveData<String>()

    fun signUpUser(name: String, email: String, password: String) {
        _isLoading.value = true
        val response: Call<DataResponse> = repository.regisUser(name, email, password)
        response.enqueue(object : Callback<DataResponse>{
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _regisUser.value = response.body()!!.message
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

    fun signInUser(email: String, password: String) {
        _isLoading.value = true
        val response: Call<User> = repository.loginUser(email, password)
        response.enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _loginUser.value = response.body()
                    message.value = response.body()!!.message
                } else {
                    message.value = response.message()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _isLoading.value = false
                message.value = t.message
            }
        })
    }
}