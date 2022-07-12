package com.example.myapplication

import android.view.View
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Utils.loadImage
import com.example.myapplication.Models.UserResponse
import com.google.android.material.tabs.TabLayoutMediator
import com.example.myapplication.Models.UseraDetailResponse
import com.example.myapplication.ViewModels.DetailViewModel
import com.example.myapplication.Adapaters.SectionPagerAdapter
import com.example.myapplication.databinding.ActivityDetailBinding

@Suppress("NAME_SHADOWING")
class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.detail_user)

        val user = intent.getParcelableExtra<UserResponse>(KEY_USER)
        user?.let { setTabLayoutAdapter(it) }
        user?.let { viewModel.userDetail(it) }
        viewModel.userdetail.observe(this) { user ->
            setUserDetail(user)
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setUserDetail(user: UseraDetailResponse) {
        binding.apply {
            avatarUser.loadImage(user.avatarUrl)
            nameUser.text = user.name
            usernameUser.text = StringBuilder(resources.getString(R.string.siput)).append(user.login)
            companyUser.text = user.company
            locationUser.text = user.location
        }
    }

    private fun setTabLayoutAdapter(user: UserResponse) {
        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.model = user
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position],)
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val KEY_USER = "user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
    }
}
