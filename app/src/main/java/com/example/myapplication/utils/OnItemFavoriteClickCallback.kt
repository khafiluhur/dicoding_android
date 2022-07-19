package com.example.myapplication.utils

import com.example.myapplication.database.Favorite
import com.example.myapplication.models.UserResponse

interface OnItemFavoriteClickCallback {
    fun onItemClicked(user: Favorite)
}