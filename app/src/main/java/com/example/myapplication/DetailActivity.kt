package com.example.myapplication

import retrofit2.Call
import android.view.View
import android.os.Bundle
import retrofit2.Callback
import retrofit2.Response
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Utils.loadImage
import com.example.myapplication.Utils.ApiConfig
import com.example.myapplication.Models.UserResponse
import com.google.android.material.tabs.TabLayoutMediator
import com.example.myapplication.Adapater.SectionPagerAdapter
import com.example.myapplication.Models.UseraDetailResponse
import com.example.myapplication.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.detail_user)

        val user = intent.getParcelableExtra<UserResponse>(KEY_USER)
        user?.let { setTabLayoutAdapter(it) }
        user?.let { setUserDetail(it) }
    }

    private fun setUserDetail(userResponse: UserResponse) {
        // Untuk kode yang bersifat logic seperti request api dll sebaiknya dipindahkan kedalam class ViewModel ya tujuannya untuk memisahkan kode yang bersifat logic dan kode untuk memanipulasi view.
        // Sesuaikan pada class yang lainnya.
        showLoading(true)
        val client = userResponse.login?.let { ApiConfig.getApiService().getDetailUser(it) }
        client?.enqueue(object : Callback<UseraDetailResponse> {
            override fun onResponse(
                call: Call<UseraDetailResponse>,
                response: Response<UseraDetailResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserDetail(responseBody)
                    }
                }
            }
            override fun onFailure(call: Call<UseraDetailResponse>, t: Throwable) {
                showLoading(false)
            }

        })
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
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
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
