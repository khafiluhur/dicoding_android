package com.example.myapplication

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.list_user)

        list.addAll(listUsers)
        showRecyclerList()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            /*
            Gunakan method ini ketika search selesai atau OK
             */
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                searchView.clearFocus()
                return true
            }

            /*
            Gunakan method ini untuk merespon tiap perubahan huruf pada searchView
             */
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private val listUsers: ArrayList<User>
    @SuppressLint("Recycle")
    get() {
        val dataName = resources.getStringArray(R.array.name)
        val dataFollower = resources.getStringArray(R.array.followers)
        val dataPhoto = resources.obtainTypedArray(R.array.avatar)
        val dataFollowing = resources.getStringArray(R.array.following)
        val dataRepository = resources.getStringArray(R.array.repository)
        val dataCompany = resources.getStringArray(R.array.company)
        val dataUsername = resources.getStringArray(R.array.username)
        val dataLocation = resources.getStringArray(R.array.location)
        val listUser = ArrayList<User>()
        for (i in dataName.indices) {
            val user = User(dataName[i],dataFollower[i],dataPhoto.getResourceId(i, -1),dataFollowing[i],dataRepository[i],dataCompany[i],dataUsername[i],dataLocation[i])
            listUser.add(user)
        }
        return listUser
    }

    private fun showRecyclerList() {
        binding.rvUser.layoutManager = if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(this, 2)
        } else {
            LinearLayoutManager(this)
        }
        val listUserAdapater = ListUserAdapater(list)
        binding.rvUser.adapter = listUserAdapater
        binding.rvUser.setHasFixedSize(true)

        listUserAdapater.setOnItemClickCallback(object : ListUserAdapater.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intentToDetail = Intent(this@MainActivity, DetailActivity::class.java)
                intentToDetail.putExtra(DetailActivity.AVATAR, data.avatar)
                intentToDetail.putExtra(DetailActivity.NAME, data.name)
                intentToDetail.putExtra(DetailActivity.USERNAME, data.username)
                intentToDetail.putExtra(DetailActivity.LOCATION, data.location)
                intentToDetail.putExtra(DetailActivity.COMPANY, data.company)
                intentToDetail.putExtra(DetailActivity.FOLLOWING, data.following)
                intentToDetail.putExtra(DetailActivity.FOLLOWER, data.follower)
                intentToDetail.putExtra(DetailActivity.REPOSITORY, data.repository)
                startActivity(intentToDetail)
            }
        })
    }
}