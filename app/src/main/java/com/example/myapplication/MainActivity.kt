@file:Suppress("UNUSED_EXPRESSION")

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
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.utils.ApiConfig
import com.example.myapplication.models.UserResponse
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.models.SearchResponse
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapaters.ListUserAdapater
import com.example.myapplication.utils.OnItemClickCallback
import com.example.myapplication.utils.SettingPreferences
import com.example.myapplication.viewModels.SettingViewModel
import com.example.myapplication.viewModels.SettingViewModelFactory
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.utils.Constant
import kotlinx.coroutines.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constant.Settings)

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    var jobSearch: Job? = null

    private val adapter: ListUserAdapater by lazy {
        ListUserAdapater()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.list_user)
        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]
        mainViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                false
            }
        }
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
                jobSearch?.cancel()
                if (newText.isNotEmpty()) {
                    jobSearch = lifecycleScope.launch(Dispatchers.Main) {
                        delay(delay)
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

                    }
                } else {
                    jobSearch = lifecycleScope.launch(Dispatchers.Main) {
                        delay(delay)
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
                }
                return false
            }
        })

        menu.findItem(R.id.favorite).setOnMenuItemClickListener {
            val intentFavorite = Intent(this@MainActivity, FavoriteActivity::class.java)
            startActivity(intentFavorite)
            true
        }
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
                intentDetail.putExtra(DetailActivity.KEY_ACTIVITY, "main")
                startActivity(intentDetail)
            }
        })
    }

    companion object {
        const val delay = 3000L
    }

}
