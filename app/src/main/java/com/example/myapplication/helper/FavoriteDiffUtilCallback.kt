package com.example.myapplication.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.database.Favorite

class FavoriteDiffUtilCallback(private val mOldFavorite: List<Favorite>, private val mNewFavorite: List<Favorite>) :DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavorite.size
    }

    override fun getNewListSize(): Int {
        return mNewFavorite.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavorite[oldItemPosition].id == mNewFavorite[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavorite = mOldFavorite[oldItemPosition]
        val newFavorite = mNewFavorite[newItemPosition]
        return oldFavorite.login == newFavorite.login && oldFavorite.url == newFavorite.url
    }

}