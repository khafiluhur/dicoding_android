package com.example.myapplication.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.myapplication.database.Favorite
import com.example.myapplication.repositorys.FavoriteRepository

class FavoriteAddUpdateViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun insert(favorite: Favorite) {
        mFavoriteRepository.insert(favorite)
    }

    fun delete(favorite: Favorite) {
        mFavoriteRepository.delete(favorite)
    }
}