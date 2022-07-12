package com.example.myapplication

import retrofit2.Call
import android.os.Bundle
import android.view.Menu
import android.view.View
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import android.content.Context
import android.widget.SearchView
import android.app.SearchManager
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Utils.ApiConfig
import com.example.myapplication.Models.UserResponse
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.Models.SearchResponse
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapater.ListUserAdapater
import com.example.myapplication.Utils.OnItemClickCallback
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val adapter: ListUserAdapater by lazy {
        ListUserAdapater()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.list_user)
        listUser()
    }

    private fun listUser() {
        showLoading(true)
        val client = ApiConfig.getApiService().getListUser()
        client.enqueue(object : Callback<ArrayList<UserResponse>> {
            override fun onResponse(
                call: Call<ArrayList<UserResponse>>,
                response: Response<ArrayList<UserResponse>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        adapter.addDataToList(responseBody)
                        showRecyclerList()
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<UserResponse>>, t: Throwable) {
                showLoading(false)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                val client = ApiConfig.getApiService().getSearch(query)
                client.enqueue(object : Callback<SearchResponse> {
                    override fun onResponse(
                        call: Call<SearchResponse>,
                        response: Response<SearchResponse>
                    ) {
                        showLoading(false)
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                adapter.addDataToList(responseBody.items)
                                showRecyclerList()
                            }
                        }
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        showLoading(false)
                    }
                })
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                showLoading(true)
                val client = ApiConfig.getApiService().getSearch(newText)
                client.enqueue(object : Callback<SearchResponse> {
                    override fun onResponse(
                        call: Call<SearchResponse>,
                        response: Response<SearchResponse>
                    ) {
                        showLoading(false)
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                adapter.addDataToList(responseBody.items)
                                showRecyclerList()
                            }
                        }
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        showLoading(false)
                    }
                })
                return false
            }
        })
        return true
    }

    private fun showRecyclerList() {
        binding.rvUser.layoutManager = if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(this, 2)
        } else {
            LinearLayoutManager(this)
        }
        binding.rvUser.adapter = adapter
        binding.rvUser.setHasFixedSize(true)
        adapter.setOnItemClickCallback(object : OnItemClickCallback {
            override fun onItemClicked(user: UserResponse) {
                val intentDetail = Intent(this@MainActivity, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.KEY_USER, user)
                startActivity(intentDetail)
            }
        })
    }
}
