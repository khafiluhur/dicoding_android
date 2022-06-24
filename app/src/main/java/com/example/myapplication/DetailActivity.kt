package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    companion object {
        const val AVATAR = "avatar"
        const val NAME = "name"
        const val USERNAME = "username"
        const val LOCATION = "location"
        const val COMPANY = "company"
        const val FOLLOWING = "following"
        const val FOLLOWER = "follower"
        const val REPOSITORY = "follower"
    }
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.detail_user)

        val avatar = intent.getSerializableExtra(AVATAR)
        val name = intent.getStringExtra(NAME)
        val username = intent.getStringExtra(USERNAME)
        val location = intent.getStringExtra(LOCATION)
        val company = intent.getStringExtra(COMPANY)
        val following = intent.getStringExtra(FOLLOWING)
        val follower = intent.getStringExtra(FOLLOWER)
        val repository = intent.getStringExtra(REPOSITORY)

        Glide.with(this)
            .load(avatar)
            .circleCrop()
            .into(binding.avatarUser)
        binding.apply {
            nameUser.text = name
            usernameUser.text = StringBuilder(resources.getString(R.string.siput)).append(username)
            companyUser.text = company
            repositoryUser.text = repository
            followingUser.text = following
            followerUser.text = follower
            locationUser.text = location
        }
    }
}
