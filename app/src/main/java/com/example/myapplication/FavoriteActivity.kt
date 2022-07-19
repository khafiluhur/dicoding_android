package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapaters.FavoriteAdapter
import com.example.myapplication.database.Favorite
import com.example.myapplication.databinding.ActivityFavoriteBinding
import com.example.myapplication.utils.OnItemFavoriteClickCallback
import com.example.myapplication.viewModels.FavoriteViewModel
import com.example.myapplication.viewModels.FavoriteViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter : FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar

        actionBar?.title = resources.getString(R.string.favorite)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val mainViewModel = obtainViewModel(this@FavoriteActivity)
        mainViewModel.getAllFavorite().observe(this) { favoriteList ->
            if (favoriteList != null) {
                adapter.setListFavorite(favoriteList)
            }
        }

        adapter = FavoriteAdapter()
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter
        adapter.setOnItemFavoriteClickCallback(object: OnItemFavoriteClickCallback {
            override fun onItemClicked(user: Favorite) {
                val intentDetail = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.KEY_USER, user)
                intentDetail.putExtra(DetailActivity.KEY_ACTIVITY, "favorite")
                startActivity(intentDetail)
            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}