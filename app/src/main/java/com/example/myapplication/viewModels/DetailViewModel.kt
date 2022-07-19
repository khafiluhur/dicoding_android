package com.example.myapplication.viewModels

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.database.Favorite
import com.example.myapplication.utils.ApiConfig
import com.example.myapplication.models.UserResponse
import com.example.myapplication.models.UserDetailResponse

class DetailViewModel : ViewModel() {

    private val _userDetail = MutableLiveData<UserDetailResponse>()
    val userDetail: LiveData<UserDetailResponse> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun userDetail(userResponse: UserResponse) {
        _isLoading.value = true
        val client = userResponse.login?.let { ApiConfig.getApiService().getDetailUser(it) }
        client?.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _userDetail.value = response.body()
                }
            }
            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun userDetailFavorite(favorite: Favorite) {
        _isLoading.value = true
        val client = favorite.login?.let { ApiConfig.getApiService().getDetailUser(it) }
        client?.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _userDetail.value = response.body()
                }
            }
            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

}