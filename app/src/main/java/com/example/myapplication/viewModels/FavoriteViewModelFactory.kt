package com.example.myapplication.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FavoriteViewModelFactory private constructor(private val mApplication: Application) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(FavoriteAddUpdateViewModel::class.java)) {
            return FavoriteAddUpdateViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

        companion object {
            @Volatile
            private var INSTANCE: FavoriteViewModelFactory? = null

            @JvmStatic
            fun getInstance(application: Application): FavoriteViewModelFactory {
                if (INSTANCE == null) {
                    synchronized(FavoriteViewModelFactory::class.java) {
                        INSTANCE = FavoriteViewModelFactory(application)
                    }
                }
                return INSTANCE as FavoriteViewModelFactory
            }
        }
}