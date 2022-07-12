package com.example.myapplication.Utils

import com.example.myapplication.Models.UserResponse

interface OnItemClickCallback {
    fun onItemClicked(user: UserResponse)
}