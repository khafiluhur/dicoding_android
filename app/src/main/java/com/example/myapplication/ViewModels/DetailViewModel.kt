package com.example.myapplication.ViewModels

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.Utils.ApiConfig
import com.example.myapplication.Models.UserResponse
import com.example.myapplication.Models.UseraDetailResponse

class DetailViewModel : ViewModel() {

    private val _userdetail = MutableLiveData<UseraDetailResponse>()
    val userdetail: LiveData<UseraDetailResponse> = _userdetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun userDetail(userResponse: UserResponse) {
        _isLoading.value = true
        val client = userResponse.login?.let { ApiConfig.getApiService().getDetailUser(it) }
        client?.enqueue(object : Callback<UseraDetailResponse> {
            override fun onResponse(
                call: Call<UseraDetailResponse>,
                response: Response<UseraDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userdetail.value = response.body()
                }
            }
            override fun onFailure(call: Call<UseraDetailResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}