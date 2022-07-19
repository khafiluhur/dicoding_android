package com.example.myapplication.utils

import retrofit2.Call
import retrofit2.http.*
import com.example.myapplication.models.*
import com.example.myapplication.BuildConfig

interface ApiService {

    @GET("users")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    fun getListUser(): Call<ArrayList<UserResponse>>

    @GET("users/{username}")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    fun getByIdUser(@Path("username") username: String): Call<UserResponse>

    @GET("search/users")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    fun getSearch(@Query("q") username: String): Call<SearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/{type}")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    fun getFollowerFollowing(
        @Path("username") username: String,
        @Path("type") type: String
    ): Call<ArrayList<UserResponse>>

    companion object {
        private const val API_TOKEN = BuildConfig.API_TOKEN
    }
}
