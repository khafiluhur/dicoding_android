package com.example.myapplication.utils

import com.example.myapplication.models.UserResponse

interface OnItemClickCallback {
    fun onItemClicked(user: UserResponse)
}