package com.example.myapplication.Utils

import retrofit2.Call
import retrofit2.http.*
import com.example.myapplication.Models.*
import com.example.myapplication.BuildConfig

interface ApiService {

    @GET("users")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    fun getListUser(): Call<ArrayList<UserResponse>>

    @GET("search/users")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    fun getSearch(@Query("q") username: String): Call<SearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<UseraDetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    fun getFollower(
        @Path("username") username: String
    ): Call<ArrayList<UserResponse>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<UserResponse>>

    companion object {
        private const val API_TOKEN = BuildConfig.API_TOKEN
    }
}
