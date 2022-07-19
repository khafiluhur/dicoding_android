package com.example.myapplication.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.models.UserResponse

class UserDiffUtilCallback(private val oldList: ArrayList<UserResponse>, private val newList: ArrayList<UserResponse>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].id == newList[newItemPosition].id -> true
            oldList[oldItemPosition].login == newList[newItemPosition].login -> true
            else -> false
        }
    }

}