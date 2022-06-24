package com.example.myapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val name: String,
    val follower: String,
    val avatar: Int,
    val following: String,
    val repository: String,
    val company: String,
    val username: String,
    val location: String
) : Parcelable
