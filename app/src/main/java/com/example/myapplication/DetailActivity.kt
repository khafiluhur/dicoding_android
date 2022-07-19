package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.utils.loadImage
import com.example.myapplication.models.UserResponse
import com.google.android.material.tabs.TabLayoutMediator
import com.example.myapplication.models.UserDetailResponse
import com.example.myapplication.adapaters.SectionPagerAdapter
import com.example.myapplication.database.Favorite
import com.example.myapplication.utils.SettingPreferences
import com.example.myapplication.databinding.ActivityDetailBinding
import com.example.myapplication.utils.ApiConfig
import com.example.myapplication.utils.Constant
import com.example.myapplication.viewModels.*
import retrofit2.Call
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constant.Settings)

@Suppress("NAME_SHADOWING", "UNUSED_EXPRESSION")
class DetailActivity : AppCompatActivity() {

    private lateinit var favoriteAddUpdateViewModel: FavoriteAddUpdateViewModel
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activity = intent.getStringExtra(KEY_ACTIVITY)
        if (activity == "favorite") {
            val actionBar = supportActionBar
            actionBar?.title = resources.getString(R.string.detail_user_favorite)
            actionBar?.setDisplayHomeAsUpEnabled(true)
            val user = intent.getParcelableExtra<Favorite>(KEY_USER)
            user?.let { viewModel.userDetailFavorite(it) }
            if (user != null) {
                detailFavorite(user)
            }
            viewModel.userDetail.observe(this) { user ->
                setUserDetail(user)
            }
            binding.fabFavorite.visibility = View.GONE
        } else {
            supportActionBar?.title = resources.getString(R.string.detail_user)
            val user = intent.getParcelableExtra<UserResponse>(KEY_USER)
            user?.let { setTabLayoutAdapter(it) }
            user?.let { viewModel.userDetail(it) }
            viewModel.userDetail.observe(this) { user ->
                setUserDetail(user)
            }
            // Favorite
            binding.fabFavorite.imageTintList = ColorStateList.valueOf(Color.rgb(255,255,255))
            val favorite = Favorite()
            favoriteAddUpdateViewModel = obtainViewModel(this@DetailActivity)
            binding.fabFavorite.setOnClickListener {
                val url = user?.avatarUrl
                val login = user?.login
                if (favorite.login != login) {
                    favorite.let {
                        favorite.url = url
                        favorite.login = login
                    }
                    favoriteAddUpdateViewModel.insert(favorite)
                    showToast(getString(R.string.added))
                } else {
                    showToast(getString(R.string.same))
                }
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        // Themes Preferences
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
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteAddUpdateViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteAddUpdateViewModel::class.java]
    }

    private fun setUserDetail(user: UserDetailResponse) {
        binding.apply {
            avatarUser.loadImage(user.avatarUrl)
            nameUser.text = user.name
            usernameUser.text = StringBuilder(resources.getString(R.string.siput)).append(user.login)
            if (user.company != null) {
                companyUser.visibility = View.VISIBLE
                companyUser.text = user.company
            }
            if (user.location != null) {
                locationUser.visibility = View.VISIBLE
                locationUser.text = user.location
            }
        }
    }

    private fun detailFavorite(favorite: Favorite) {
        val client = favorite.login?.let { ApiConfig.getApiService().getByIdUser(it) }
        client?.enqueue(object : retrofit2.Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setTabLayoutAdapter(responseBody)
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    private fun setTabLayoutAdapter(user: UserResponse) {
        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.model = user
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_option_menu, menu)
        menu.findItem(R.id.setting).setOnMenuItemClickListener {
            val intentSetting = Intent(this@DetailActivity, SettingActivity::class.java)
            startActivity(intentSetting)
            true
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val KEY_USER = "user"
        const val KEY_ACTIVITY = "activity"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
    }
}
